/*
 * Copyright (c) 2013 Android Alliance, LTD
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
package com.glasstunes.screenslide;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.DisplayMetrics;
import android.util.Log;

public class ContextWrapperEdgeEffect extends ContextWrapper {

	private ResourcesEdgeEffect mResourcesEdgeEffect;

	public ContextWrapperEdgeEffect(Context context) {
		super(context);
		Resources resources = context.getResources();
		mResourcesEdgeEffect = new ResourcesEdgeEffect(resources.getAssets(),
				resources.getDisplayMetrics(), resources.getConfiguration());
	}

	@Override
	public Resources getResources() {
		return mResourcesEdgeEffect;
	}

	private class ResourcesEdgeEffect extends Resources {
		private int overscroll_edge = getPlatformDrawableId("overscroll_edge");
		private int overscroll_glow = getPlatformDrawableId("overscroll_glow");

		public ResourcesEdgeEffect(AssetManager assets, DisplayMetrics metrics,
				Configuration config) {
			// super(metrics, localConfiguration);
			super(assets, metrics, config);
		}

		private int getPlatformDrawableId(String name) {
			try {
				int i = ((Integer) Class
						.forName("com.android.internal.R$drawable")
						.getField(name).get(null)).intValue();
				return i;
			} catch (ClassNotFoundException e) {
				Log.e("[ContextWrapperEdgeEffect].getPlatformDrawableId()",
						"Cannot find internal resource class");
				return 0;
			} catch (NoSuchFieldException e1) {
				Log.e("[ContextWrapperEdgeEffect].getPlatformDrawableId()",
						"Internal resource id does not exist: " + name);
				return 0;
			} catch (IllegalArgumentException e2) {
				Log.e("[ContextWrapperEdgeEffect].getPlatformDrawableId()",
						"Cannot access internal resource id: " + name);
				return 0;
			} catch (IllegalAccessException e3) {
				Log.e("[ContextWrapperEdgeEffect].getPlatformDrawableId()",
						"Cannot access internal resource id: " + name);
			}
			return 0;
		}

		@Override
		public Drawable getDrawable(int resId)
				throws Resources.NotFoundException {
			if (resId == this.overscroll_edge || resId == this.overscroll_glow) {
				return new ShapeDrawable();
			} else {
				return super.getDrawable(resId);
			}

		}
	}
}