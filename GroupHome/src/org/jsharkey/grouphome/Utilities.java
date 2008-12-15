/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jsharkey.grouphome;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Canvas;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.content.res.Resources;
import android.content.Context;

public class Utilities {

	public static Drawable createIconThumbnail(Drawable icon, int size) {
		// code adapted from packages/apps/Launcher
		
		int sourceWidth = icon.getIntrinsicWidth(),
			sourceHeight = icon.getIntrinsicHeight();
		
		int destWidth = size,
			destHeight = size;

		// only resize if actually needed
		if(sourceWidth != destWidth || sourceHeight != destHeight) {
			float ratio = (float) sourceWidth / sourceHeight;
			if(sourceWidth > sourceHeight) {
				destHeight = (int) (destWidth / ratio);
			} else if (sourceHeight > sourceWidth) {
				destWidth = (int) (destHeight * ratio);
			}
			
			final Bitmap thumb = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
			final Canvas canvas = new Canvas(thumb);
			
			icon.setBounds((size - destWidth) / 2, (size - destHeight) / 2, destWidth, destHeight);
			icon.draw(canvas);
			icon = new BitmapDrawable(thumb);
			
		}

		return icon;
	}
}
