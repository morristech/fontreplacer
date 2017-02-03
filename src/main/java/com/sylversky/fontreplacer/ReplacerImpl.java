/*
 * Copyright (C) 2017 Leonardus Ardyandhita.
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

package com.sylversky.fontreplacer;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReplacerImpl implements Replacer {

    private Context context;

    protected ReplacerImpl(Context context){
        this.context = context;
    }

    @Override
    public void setDefaultFont(String defaultFontAsset) {
        FontReplacer.defaultFont = Typeface.createFromAsset(context.getAssets(), defaultFontAsset);
    }

    @Override
    public void setBoldFont(String boldFontAsset) {
        FontReplacer.boldFont = Typeface.createFromAsset(context.getAssets(), boldFontAsset);
    }

    @Override
    public void setItalicFont(String italicFontAsset) {
        FontReplacer.italicFont = Typeface.createFromAsset(context.getAssets(), italicFontAsset);
    }

    @Override
    public void setBoldItalicFont(String boldItalicFontAsset) {
        FontReplacer.boldItalicFont = Typeface.createFromAsset(context.getAssets(), boldItalicFontAsset);
    }

    @Override
    public void setLightFont(String lightFontAsset) {
        FontReplacer.lightFont = Typeface.createFromAsset(context.getAssets(), lightFontAsset);
    }

    @Override
    public void setCondensedFont(String condensedFontAsset) {
        FontReplacer.condensedFont = Typeface.createFromAsset(context.getAssets(), condensedFontAsset);
    }

    @Override
    public void setThinFont(String thinFontAsset) {
        FontReplacer.thinFont = Typeface.createFromAsset(context.getAssets(), thinFontAsset);
    }

    @Override
    public void setMediumFont(String mediumFontAsset) {
        FontReplacer.mediumFont = Typeface.createFromAsset(context.getAssets(), mediumFontAsset);
    }

    @Override
    public void applyFont() {
        if(!validateDefaultFont()){
            Log.e("Cannot set custom font", "at least default font must be set");
            return;
        }

        if(Build.VERSION.SDK_INT >= 21) {
            Map<String, Typeface> fontsMap = new HashMap<>();
            fontsMap.put("sans-serif", FontReplacer.defaultFont);
            fontsMap.put("sans-serif-bold", FontReplacer.boldFont);
            fontsMap.put("sans-serif-italic", FontReplacer.italicFont);
            fontsMap.put("sans-serif-light", FontReplacer.lightFont);
            fontsMap.put("sans-serif-condensed", FontReplacer.condensedFont);
            fontsMap.put("sans-serif-thin", FontReplacer.thinFont);
            fontsMap.put("sans-serif-medium", FontReplacer.mediumFont);

            try {
                Field defaultFont = Typeface.class.getDeclaredField("sSystemFontMap");
                defaultFont.setAccessible(true);
                defaultFont.set(null, fontsMap);
                defaultFont.setAccessible(false);
            } catch (Exception e) {
                Log.e("Cannot set custom font", e.getMessage());
            }
        }else{
            Typeface[] sDefaults = new Typeface[] {FontReplacer.defaultFont, FontReplacer.boldFont, FontReplacer.italicFont, FontReplacer.boldItalicFont};

            try {
                Field defaultFont = Typeface.class.getDeclaredField("DEFAULT");
                Field defaultBoldFont = Typeface.class.getDeclaredField("DEFAULT_BOLD");
                Field sansSerifFont = Typeface.class.getDeclaredField("SANS_SERIF");
                Field serifFont = Typeface.class.getDeclaredField("SERIF");
                Field monospaceFont = Typeface.class.getDeclaredField("MONOSPACE");

                defaultFont.setAccessible(true);
                defaultFont.set(null, sDefaults[0]);

                defaultBoldFont.setAccessible(true);
                defaultBoldFont.set(null, null);

                sansSerifFont.setAccessible(true);
                sansSerifFont.set(null, null);

                serifFont.setAccessible(true);
                serifFont.set(null, null);

                monospaceFont.setAccessible(true);
                monospaceFont.set(null, null);

                Field defaults = Typeface.class.getDeclaredField("sDefaults");
                defaults.setAccessible(true);
                defaults.set(null, sDefaults);
            }catch (Exception e){
                Log.e("Cannot set custom font", e.getMessage());
            }
        }
    }

    private boolean validateDefaultFont(){
        if(FontReplacer.defaultFont==null){
            return false;
        }

        if(FontReplacer.boldFont == null){
            FontReplacer.boldFont = FontReplacer.defaultFont;
        }

        if(FontReplacer.italicFont == null){
            FontReplacer.italicFont = FontReplacer.defaultFont;
        }

        if(FontReplacer.boldItalicFont == null){
            FontReplacer.boldItalicFont = FontReplacer.boldFont;
        }

        if(FontReplacer.lightFont == null){
            FontReplacer.lightFont = FontReplacer.defaultFont;
        }

        if(FontReplacer.condensedFont == null){
            FontReplacer.condensedFont = FontReplacer.defaultFont;
        }

        if(FontReplacer.thinFont == null){
            FontReplacer.thinFont = FontReplacer.defaultFont;
        }

        if(FontReplacer.mediumFont == null){
            FontReplacer.mediumFont = FontReplacer.defaultFont;
        }

        return true;
    }
}
