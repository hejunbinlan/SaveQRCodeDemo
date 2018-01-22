package org.hjb.saveqrcodedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.sumimakito.awesomeqr.AwesomeQRCode;

public class MainActivity extends AppCompatActivity {
    private Context mContext;

    private ImageView qrcodeImageView;
    private Button saveQrcodeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);

        this.initView();
        this.initListener();
        this.initData();

    }

    private void initView() {
        qrcodeImageView = findViewById(R.id.qrcodeImageView);
        saveQrcodeButton = findViewById(R.id.saveQrcodeButton);
    }

    private void initListener() {
        saveQrcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQrcodeToGallery();
            }
        });

    }

    private void initData() {

        new AwesomeQRCode.Renderer()
                .contents("HTTPS://QR.ALIPAY.COM/FKX08887CNJJPX3EOUGF7B")
                .size(getResources().getDimensionPixelSize(R.dimen.qrcode_size))//二维码的大小
                .margin(getResources().getDimensionPixelSize(R.dimen.qrcode_size_margin))//二维码边距即留白大小
                .dotScale(1)//点的伸缩比例
                .logoRadius(getResources().getDimensionPixelOffset(R.dimen.qrcode_size_logo_radius))//中心logo圆角大小
                .logo(BitmapFactory.decodeResource(getResources(), R.mipmap.icon))//中心logo
                .logoScale(0.25F)//logo占二维码的比例
                .renderAsync(new AwesomeQRCode.Callback() {
                    @Override
                    public void onRendered(AwesomeQRCode.Renderer renderer, final Bitmap bitmap) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                qrcodeImageView.setImageBitmap(bitmap);
                            }
                        });
                    }

                    @Override
                    public void onError(AwesomeQRCode.Renderer renderer, Exception e) {
                        e.printStackTrace();
                    }
                });
    }


    private void saveQrcodeToGallery() {
        //创建视图
        View qrcodeView = getLayoutInflater().inflate(R.layout.qrcode_page, null, false);
        ((ImageView)qrcodeView.findViewById(R.id.qrcodeImageView)).setImageDrawable(qrcodeImageView.getDrawable());

        //计算视图大小
        DisplayMetrics displayMetrics = SystemUtils.getWindowDisplayMetrics(mContext);
        final int width = displayMetrics.widthPixels;
        final int height = displayMetrics.heightPixels - SystemUtils.getStatusBarHeight(mContext) - SystemUtils.getActionBarHeight(mContext) - getResources().getDimensionPixelSize(R.dimen.default_bottom_bar_height);

        //将视图生成图片
        Bitmap image = SystemUtils.generateImageFromView(qrcodeView, width, height);

        //将图片保存到系统相册
        boolean isSuccess = SystemUtils.saveImageToGallery(MainActivity.this, image);
        image.recycle();
        if (isSuccess) {
            Toast.makeText(MainActivity.this, "已保存到系统相册！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "保存到系统相册失败！", Toast.LENGTH_SHORT).show();
        }
    }

}
