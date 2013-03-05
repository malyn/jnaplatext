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
import java.util.Arrays;
import java.util.List;

/* JNA imports. */
import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.BaseTSD.ULONG_PTR;
import com.sun.jna.platform.win32.WinDef.HCURSOR;
import com.sun.jna.platform.win32.WinDef.HICON;
import com.sun.jna.platform.win32.WinDef.HINSTANCE;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.win32.StdCallLibrary;

/**
 * Provides JNA access to Win32 functions in WINUSER.DLL.
 *
 * @author      Michael Alyn Miller <malyn@strangeGizmo.com>
 * @version     1.0.0
 */
public interface WinUser
    extends StdCallLibrary,
            com.sun.jna.platform.win32.WinUser {

    /* -----------------------------------------------------------------
     * CreateWindowEx constants
     */

    /** Used to specify that the system should select the default x, y,
     * width, or height of the window depending on which argument this
     * value is passed to. */
    int CW_USEDEFAULT = 0x80000000;


    /* -----------------------------------------------------------------
     * Window Messages
     */

    /** Sent when an application requests that a window be created by
     * calling the CreateWindowEx or CreateWindow function. */
    int WM_CREATE = 0x0001;

    /** Sent when a window is being destroyed. */
    int WM_DESTROY = 0x0002;

    /** Sent to pass data to another application. */
    int WM_COPYDATA = 0x004A;


    /* -----------------------------------------------------------------
     * Window Styles
     */

    /** The window has a thin-line border. */
    int WS_BORDER = 0x00800000;

    /** The window has a title bar (includes the WS_BORDER style). */
    int WS_CAPTION = 0x00C00000;

    /** The windows is a pop-up window. This style cannot be used with
     * the WS_CHILD style. */
    int WS_POPUP = 0x80000000;

    /** The window has a window menu on its title bar. The WS_CAPTION
     * style must also be specified. */
    int WS_SYSMENU = 0x00080000;

    /** The window is a pop-up window. */
    int WS_POPUPWINDOW = WS_POPUP | WS_BORDER | WS_SYSMENU;


    /* -----------------------------------------------------------------
     * WinUser types.
     */

    /**
     * Contains data to be passed to another application by the
     * WM_COPYDATA message.
     */
    public static class COPYDATASTRUCT extends Structure {
        /**
         * The by-reference version of this structure.
         */
        public static class ByReference
                extends COPYDATASTRUCT
                implements Structure.ByReference { }

        /**
         * Instantiates a new COPYDATASTRUCT.
         */
        public COPYDATASTRUCT() { }

        /**
         * Instantiates a new COPYDATASTRUCT with existing data given
         * the address of that data.
         *
         * @param pointer Address of the existing structure.
         */
        public COPYDATASTRUCT(final long pointer) {
            this(new Pointer(pointer));
        }

        /**
         * Instantiates a new COPYDATASTRUCT with existing data given
         * a pointer to that data.
         *
         * @param memory Pointer to the existing structure.
         */
        public COPYDATASTRUCT(final Pointer memory) {
            super(memory);
            read();
        }

        /** The data to be passed to the receiving application. */
        public ULONG_PTR dwData;

        /** The size, in bytes, of the data pointed to by the lpData
         * member. */
        public int cbData;

        /** The data to be passed to the receiving application. This
         * member can be null. */
        public Pointer lpData;

        /**
         * Returns the serialized order of this structure's fields.
         *
         * @return The serialized order of this structure's fields.
         * @see com.sun.jna.Structure#getFieldOrder()
         */
        @Override
        protected final List getFieldOrder() {
            return Arrays.asList(new String[] {"dwData", "cbData",
                    "lpData" });
        }
    }

    /**
     * Handle to a bitmap.
     */
    public static class HBRUSH extends HANDLE {
        /**
         * Instantiates a new HBRUSH.
         */
        public HBRUSH() { }

        /**
         * Instantiates a new HBRUSH with a given pointer.
         *
         * @param p Pointer to the brush.
         */
        public HBRUSH(final Pointer p) {
            super(p);
        }
    }

    /**
     * Contains window class information.
     */
    public static class WNDCLASSEX extends Structure {
        /**
         * The by-reference version of this structure.
         */
        public static class ByReference
                extends WNDCLASSEX
                implements Structure.ByReference { }

        /**
         * Instantiates a new WNDCLASSEX.
         */
        public WNDCLASSEX() { }

        /**
         * Instantiates a new WNDCLASSEX with existing data given a
         * pointer to that data.
         *
         * @param memory Pointer to the existing structure.
         */
        public WNDCLASSEX(final Pointer memory) {
            super(memory);
            read();
        }

        /** The size, in bytes, of this structure. */
        public int cbSize = this.size();

        /** The class style(s). */
        public int style;

        /** A pointer to the window procedure. */
        public Callback lpfnWndProc;

        /** The number of extra bytes to allocate following the
         * window-class structure. */
        public int cbClsExtra;

        /** The number of extra bytes to allocate following the window
         * instance. */
        public int cbWndExtra;

        /** A handle to the instance that contains the window procedure
         * for the class. */
        public HINSTANCE hInstance;

        /** A handle to the class icon. */
        public HICON hIcon;

        /** A handle to the class cursor. */
        public HCURSOR hCursor;

        /** A handle to the class background brush. */
        public HBRUSH hbrBackground;

        /** Name of the class menu, as the name appears in the resource
         * file. */
        public String lpszMenuName;

        /** Class name or atom. */
        public WString lpszClassName;

        /** A handle to a small icon that is associated with the window
         * class. */
        public HICON hIconSm;

        /**
         * Returns the serialized order of this structure's fields.
         *
         * @return The serialized order of this structure's fields.
         * @see com.sun.jna.Structure#getFieldOrder()
         */
        @Override
        protected final List getFieldOrder() {
            return Arrays.asList(new String[] {"cbSize", "style",
                    "lpfnWndProc", "cbClsExtra", "cbWndExtra", "hInstance",
                    "hIcon", "hCursor", "hbrBackground", "lpszMenuName",
                    "lpszClassName", "hIconSm" });
        }
    }

    /**
     * An application-defined function that processes messages sent to a
     * window.
     */
    public interface WindowProc extends Callback {
        /**
         * An application-defined function that processes messages sent
         * to a window.
         *
         * @param hwnd A handle to the window.
         * @param uMsg The message.
         * @param wParam Additional message information.
         * @param lParam Additional message information.
         * @return The result of the message processing and depends on
         * the message sent
         */
        LRESULT callback(HWND hwnd, int uMsg, WPARAM wParam, LPARAM lParam);
    }
}
