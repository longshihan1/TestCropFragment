# TestCropFragment

适用于API 15+(Android 4.0.3)。
### 简介
这是一款专用于处理拍照，裁剪的库。内部支持luban压缩和自定义压缩。

### 注意
内部实现了权限处理相关的代码（存储位置权限和拍照权限）

### 使用介绍
- gradle 
```java {.class1 .class} 
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

    dependencies {
	        compile 'com.github.longshihan1:TestCropFragment:1.0.0'
	}
```


- 使用方法

```java {.class1 .class} 

 LubanOptions option=new LubanOptions.Builder()
                .setMaxHeight(180)
                .setMaxWidth(180)
                .setMaxSize(60000)
                .create();
        config= CompressConfig.ofLuban(option);

 photoGraphUtils =new PhotoGraphUtils.Builder()
                .setActivity(this)
                .setOnLoadBitmapListsner(new OnLoadBitmapListsner() {
                    @Override
                    public void onLoadBitmap(CropImage image) {
                        Glide.with(MainActivity.this).load(image.compressPath).into(iv);
                    }

                    @Override
                    public void onLoadFailure(int type, String msg) {
                        //错误
                    }
                })
                .setCompressConfig(config)
                .setCropOption(new CropOptions.Builder().setAspectX(1).setAspectY(1).setOutputX(160).setAspectY(160).create())
                .build();

 photoGraphUtils.selectTakePhoto();//拍照的方法
 photoGraphUtils.selectAlbumPhoto();//读取相册的方法
```
