package ir.tamuk.reservation.utils;

import android.view.View;
import android.widget.ScrollView;

import androidx.core.widget.NestedScrollView;

import ir.tamuk.reservation.databinding.LayoutShowMoreBinding;

public class Tools {



    public static void scrollToPosition(NestedScrollView scrollView , View targetView) {

        scrollView.postDelayed(() -> scrollView.smoothScrollTo(0, targetView.getTop()), 300);

    }
}
