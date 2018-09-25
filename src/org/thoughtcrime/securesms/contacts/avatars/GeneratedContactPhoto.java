package org.thoughtcrime.securesms.contacts.avatars;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v7.content.res.AppCompatResources;
import android.text.TextUtils;

import com.amulyakhare.textdrawable.TextDrawable;

import org.thoughtcrime.securesms.R;
import org.thoughtcrime.securesms.util.ThemeUtil;
import org.thoughtcrime.securesms.util.ViewUtil;

import java.util.regex.Pattern;

public class GeneratedContactPhoto implements FallbackContactPhoto {

  private static final Pattern  PATTERN  = Pattern.compile("[^\\p{L}\\p{Nd}\\p{P}\\p{S}]+");
  private static final Typeface TYPEFACE = Typeface.create("sans-serif-medium", Typeface.NORMAL);

  private final String name;

  public GeneratedContactPhoto(@NonNull String name) {
    this.name  = name;
  }

  @Override
  public Drawable asDrawable(Context context, int color) {
    return asDrawable(context, color, false);
  }

  @Override
  public Drawable asDrawable(Context context, int color, boolean inverted) {
    int targetSize = context.getResources().getDimensionPixelSize(R.dimen.contact_photo_target_size);

    Drawable base = TextDrawable.builder()
                                .beginConfig()
                                .width(targetSize)
                                .height(targetSize)
                                .useFont(TYPEFACE)
                                .fontSize(ViewUtil.dpToPx(context, 24))
                                .textColor(inverted ? color : Color.WHITE)
                                .endConfig()
                                .buildRound(getAbbreviation(name), inverted ? Color.WHITE : color);

    Drawable gradient = context.getResources().getDrawable(ThemeUtil.isDarkTheme(context) ? R.drawable.avatar_gradient_dark
                                                                                          : R.drawable.avatar_gradient_light);

    return new LayerDrawable(new Drawable[] { base, gradient });
  }

  private String getAbbreviation(String name) {
    String[]      parts   = name.split(" ");
    StringBuilder builder = new StringBuilder();
    int           count   = 0;

    for (int i = 0; i < parts.length && count < 2; i++) {
      String cleaned = PATTERN.matcher(parts[i]).replaceFirst("");
      if (!TextUtils.isEmpty(cleaned)) {
        builder.appendCodePoint(cleaned.codePointAt(0));
        count++;
      }
    }

    if (builder.length() == 0) {
      return "#";
    } else {
      return builder.toString();
    }
  }

  @Override
  public Drawable asCallCard(Context context) {
    return AppCompatResources.getDrawable(context, R.drawable.ic_person_large);

  }
}
