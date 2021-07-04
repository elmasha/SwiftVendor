package com.gas.swiftel.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gas.swiftel.R;

public class SlideAdpter extends PagerAdapter {

    Context mContext;
    LayoutInflater layoutInflater;

    public SlideAdpter(Context context) {
        mContext = context;
    }

    public int[] slideIamage = {

            R.drawable.place,
            R.drawable.permission,
            R.drawable.shippingdelivery
    };

    public String[] slideHeadings = {
            "Location",
            "Terms and condition",
            "Door-step delivery "
    };
    public String[] slideDesc = {
            "To get a more accurate location for your phone, turn on Location Accuracy.\n" +
                    "The app can use your location at any time.",

            "This end-user licence agreement (EULA) is a legal agreement between you (End-user or you) and \n" +
                    " SwiftGAs Digital Merchant for SwiftGas mobile application software (App)",


            "Doorstep deliveries is an on-demand delivery service based in Kenya.\n" +
                    "Our vendors will deliver gas to your doorstep",

    };

    @Override
    public int getCount() {
        return slideHeadings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImage = (ImageView) view.findViewById(R.id.SlideImage);
        TextView slideHeading = (TextView) view.findViewById(R.id.Slide_Heading);
        TextView slidedesc = (TextView) view.findViewById(R.id.Slide_Desc);

        slideImage.setImageResource(slideIamage[position]);
        slideHeading.setText(slideHeadings[position]);
        slidedesc.setText(slideDesc[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
