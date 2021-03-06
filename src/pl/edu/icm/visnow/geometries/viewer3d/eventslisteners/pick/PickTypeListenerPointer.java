//<editor-fold defaultstate="collapsed" desc=" COPYRIGHT AND LICENSE ">
/* VisNow
 Copyright (C) 2006-2013 University of Warsaw, ICM

 This file is part of GNU Classpath.

 GNU Classpath is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2, or (at your option)
 any later version.

 GNU Classpath is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with GNU Classpath; see the file COPYING.  If not, write to the
 University of Warsaw, Interdisciplinary Centre for Mathematical and
 Computational Modelling, Pawinskiego 5a, 02-106 Warsaw, Poland.

 Linking this library statically or dynamically with other modules is
 making a combined work based on this library.  Thus, the terms and
 conditions of the GNU General Public License cover the whole
 combination.

 As a special exception, the copyright holders of this library give you
 permission to link this library with independent modules to produce an
 executable, regardless of the license terms of these independent
 modules, and to copy and distribute the resulting executable under
 terms of your choice, provided that you also meet, for each linked
 independent module, the terms and conditions of the license of that
 module.  An independent module is a module which is not derived from
 or based on this library.  If you modify this library, you may extend
 this exception to your version of the library, but you are not
 obligated to do so.  If you do not wish to do so, delete this
 exception statement from your version. */
//</editor-fold>
package pl.edu.icm.visnow.geometries.viewer3d.eventslisteners.pick;

import java.util.EnumMap;
import pl.edu.icm.visnow.lib.basic.viewers.HapticViewer3D.controller.pointer3d.IViewPointer;

/**
 * Implemented by pointers that want to be notified about changes in active pick types.
 * <p/>
 * @author Łukasz Czerwiński <czerwinskilukasz1 [#] gmail.com>, ICM, University of Warsaw, 2013
 */
public interface PickTypeListenerPointer extends IViewPointer {

    /**
     * Called when a pointer is attached to the viewer and should get the first list of active
     * picks.
     * <p/>
     * @param pickTypes all pick types with counters meaning how many modules has those picks
     *                  enabled
     */
    public void onPicksInit(EnumMap<PickType, Integer> pickTypes);

    /**
     * Called when a module has enabled
     * <code>pickType</code> picking type. It DOES NOT mean that this type of pick hadn't been
     * enabled earlier by another module.
     * <p/>
     * @param pickType  just enabled pick type
     * @param pickTypes all pick types with counters meaning how many modules has those picks
     *                  enabled
     */
    public void onPickTurnedOn(PickType pickType, EnumMap<PickType, Integer> pickTypes);

    /**
     * Called when a module has disabled
     * <code>pickType</code> picking type. It DOES NOT mean that this type of pick isn't still
     * active - it can be active in another module.
     * <p/>
     * @param pickType  just disabled pick type
     * @param pickTypes all pick types with counters meaning how many modules has those picks
     *                  enabled
     */
    public void onPickTurnedOff(PickType pickType, EnumMap<PickType, Integer> pickTypes);
}
