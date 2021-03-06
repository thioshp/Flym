/**
 * Flym
 * <p/>
 * Copyright (c) 2012-2015 Frederic Julian
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ru.yanus171.feedexfork.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import java.util.ArrayList;

public class AutoSummaryEditPreference extends android.preference.EditTextPreference {

    private CharSequence mSummary;
    public AutoSummaryEditPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSummary = getSummary();
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            setSummary(GetSummary());
        }
    }

    private CharSequence GetSummary() {
        ArrayList<CharSequence> list = new ArrayList<>();
        if ( mSummary != null  )
            list.add( mSummary );
        if ( getText() != null  )
            list.add( getText().replace( "%", "%%" ) );
        return TextUtils.join( "\n", list );
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        super.onSetInitialValue(restoreValue, defaultValue);
        setSummary(GetSummary());
    }
}
