package sg.edu.np.mad.nearbuy;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import java.util.List;


public class FullScreenImageAdapter extends PagerAdapter {


    private Context context;
    private List<Integer> images;
    private LayoutInflater inflater;


    public FullScreenImageAdapter(Context context, List<Integer> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return images.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View imageLayout = inflater.inflate(R.layout.full_screen_image, container, false);


        ImageView imageView = imageLayout.findViewById(R.id.image);


        imageView.setImageResource(images.get(position));


        container.addView(imageLayout, 0);


        return imageLayout;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}