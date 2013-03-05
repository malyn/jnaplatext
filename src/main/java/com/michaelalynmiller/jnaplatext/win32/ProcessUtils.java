/*
 * Copyright (c) 2013, Michael Alyn Miller <malyn@strangeGizmo.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice unmodified, this list of conditions, and the following
 *    disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of Michael Alyn Miller nor the names of the
 *    contributors to this software may be used to endorse or promote
 *    products derived from this software without specific prior written
 *    permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

package com.michaelalynmiller.jnaplatext.win32;

/* Java imports. */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* JNA imports. */
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;

/**
 * Utilities for enumerating over and working with Win32 processes.
 *
 * @author      Michael Alyn Miller <malyn@strangeGizmo.com>
 * @version     1.0.0
 */
public final class ProcessUtils {
    /**
     * Prevent construction.
     */
    private ProcessUtils() { }

    /**
     * Gets the list of processes on this machine.
     *
     * @return The list of processes on this machine.
     */
    public static List<ProcessInfo> getProcessList()
            throws Exception {
        /* Initialize the empty process list. */
        List<ProcessInfo> processList = new ArrayList<ProcessInfo>();

        /* Create the process snapshot. */
        HANDLE snapshot = Kernel32.INSTANCE.CreateToolhelp32Snapshot(
                Tlhelp32.TH32CS_SNAPPROCESS, new DWORD(0));

        Tlhelp32.PROCESSENTRY32.ByReference pe
            = new Tlhelp32.PROCESSENTRY32.ByReference();
        for (boolean more = Kernel32.INSTANCE.Process32First(snapshot, pe);
                more;
                more = Kernel32.INSTANCE.Process32Next(snapshot, pe)) {
            /* Open this process; ignore processes that we cannot open. */
            HANDLE hProcess = Kernel32.INSTANCE.OpenProcess(
                    0x1000, /* PROCESS_QUERY_LIMITED_INFORMATION */
                    false,
                    pe.th32ProcessID.intValue());
            if (hProcess == null) {
                continue;
            }

            /* Get the image name. */
            char[] imageNameChars = new char[1024];
            IntByReference imageNameLen
                = new IntByReference(imageNameChars.length);
            if (!Kernel32.INSTANCE.QueryFullProcessImageName(
                    hProcess, new DWORD(0), imageNameChars, imageNameLen)) {
                throw new Exception("Couldn't get process image name for "
                        + pe.th32ProcessID.intValue());
            }

            /* Add the process info to our list. */
            processList.add(new ProcessInfo(
                pe.th32ProcessID.intValue(),
                pe.th32ParentProcessID.intValue(),
                new String(imageNameChars, 0, imageNameLen.getValue())));

            /* Close the process handle. */
            Kernel32.INSTANCE.CloseHandle(hProcess);
        }

        /* Close the process snapshot. */
        Kernel32.INSTANCE.CloseHandle(snapshot);

        /* Return the process list. */
        return processList;
    }

    /**
     * Returns the current process's ancestors in ascending (towards the
     * root) order.
     *
     * @return The list of this process's ancestors.
     */
    public static List<ProcessInfo> getProcessAncestors()
            throws Exception {
        return getProcessAncestors(getProcessList());
    }

    /**
     * Given a list of the processes on this machine, returns the
     * current process's ancestors in ascending (towards the root)
     * order.
     *
     * @param processList The list of processes on this machine.
     * @return The list of this process's ancestors.
     */
    public static List<ProcessInfo> getProcessAncestors(
            final List<ProcessInfo> processList) {
        /* Convert the list to a HashMap indexed by process id. */
        HashMap<Integer, ProcessInfo> processListById
            = new HashMap<Integer, ProcessInfo>();
        for (ProcessInfo entry : processList) {
            processListById.put(entry.getProcessId(), entry);
        }

        /* Initialize the empty ancestor list. */
        List<ProcessInfo> ancestorList = new ArrayList<ProcessInfo>();

        /* Walk up the PID chain and build our ancestor list. */
        ProcessInfo curProcess = processListById.get(
                Kernel32.INSTANCE.GetCurrentProcessId());
        while (curProcess != null) {
            /* Add this entry to the end of the ancestor list. */
            ancestorList.add(curProcess);

            /* Next process. */
            curProcess = processListById.get(curProcess.getParentProcessId());
        }

        /* Return the ancestor list. */
        return ancestorList;
    }

    /**
     * Returns the list of windows owned by the given process.
     *
     * @param processId Process id whose windows should be returned.
     * @return The list of windows owned by the given process.
     */
    public static List<HWND> getProcessWindows(final int processId)
            throws Exception {
        /* Initialize the empty window list. */
        final List<HWND> processWindows = new ArrayList<HWND>();

        /* Enumerate all of the windows and add all of the one for the
         * given process id to our list. */
        boolean result = User32.INSTANCE.EnumWindows(
            new WinUser.WNDENUMPROC() {
                public boolean callback(
                        final HWND hwnd, final Pointer data) {
                    /* Get the process id associated with this window. */
                    IntByReference windowPid = new IntByReference();
                    User32.INSTANCE.GetWindowThreadProcessId(hwnd, windowPid);

                    /* Add the window to our list if it is associated
                     * with the desired process id. */
                    if (windowPid.getValue() == processId) {
                        processWindows.add(hwnd);
                    }

                    /* Keep searching. */
                    return true;
                }
            },
            null);

        /* Handle errors. */
        if (!result && Kernel32.INSTANCE.GetLastError() != 0) {
            throw new Exception("Couldn't enumerate windows.");
        }

        /* Return the window list. */
        return processWindows;
    }

    /**
     * Stores the information about a Win32 process.
     */
    public static final class ProcessInfo {
        /** Process id. */
        private int processId;

        /** Parent process id. */
        private int parentProcessId;

        /** Path to this process's image. */
        private String imageName;

        /**
         * Constructs a new ProcessInfo object.
         *
         * @param processId Process id.
         * @param parentProcessId Parent process id.
         * @param imageName Process image name.
         */
        public ProcessInfo(
                final int processId,
                final int parentProcessId,
                final String imageName) {
            this.processId = processId;
            this.parentProcessId = parentProcessId;
            this.imageName = imageName;
        }

        /**
         * Returns the process id.
         *
         * @return The process id.
         */
        public int getProcessId() {
            return processId;
        }

        /**
         * Returns the parent process id.
         *
         * @return The parent process id.
         */
        public int getParentProcessId() {
            return parentProcessId;
        }

        /**
         * Returns the image name.
         *
         * @return The image name.
         */
        public String getImageName() {
            return imageName;
        }
    }
}
