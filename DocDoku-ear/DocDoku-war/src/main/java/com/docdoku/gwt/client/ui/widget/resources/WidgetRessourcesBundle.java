/*
 * WidgetRessourcesBundle.java
 * 
 * Copyright (c) 2009 Docdoku. All rights reserved.
 * 
 * This file is part of Docdoku.
 * 
 * Docdoku is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Docdoku is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Docdoku.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.docdoku.gwt.client.ui.widget.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 *
 * @author Emmanuel Nhan {@literal <emmanuel.nhan@insa-lyon.fr>}
 */
public interface WidgetRessourcesBundle extends ClientBundle{

    @Source("com/docdoku/gwt/client/ui/widget/resources/navigate_up_small.png")
    ImageResource getSmallUpImage();

    @Source("com/docdoku/gwt/client/ui/widget/resources/navigate_down_small.png")
    ImageResource getSmallDownImage();

    @Source("com/docdoku/gwt/client/ui/widget/resources/grippy.png")
    ImageResource dragNDropIcon();

}