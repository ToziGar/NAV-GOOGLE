# Android-GmailNavigationDrawer
_An example illustrating how to implement a Navigation Drawer similar to the one in Google's Gmail App in Android_

Google's Gmail App mostly follows the standard navigation drawer you get with the standard material design library. However, there are several  features that sets it apart:
1. Drop down arrow in the drawer's header
2. When clicked, another drawer menu is shown.
3. Counters in the menu itmes. 

This article will show you how to implement those features.

If you are not sure of how to implement a standard navigation drawer, you can click these links:
1. [Create a navigation drawer](https://developer.android.com/training/implementing-navigation/nav-drawer) - Official documentation on how to create a navigation drawer
2. [wRorsjakz/Android-NavigationDrawer](https://github.com/wRorsjakz/Android-NavigationDrawer) - My other Github repository on how to implement a navigation drawer

<br>
<img src="https://img.shields.io/badge/minSdkVersion-21-red.svg?style=true" alt="minSdkVersion 21" data-canonical-src="https://img.shields.io/badge/minSdkVersion-24-red.svg?style=true" style="max-width:100%;">
<img src=https://img.shields.io/badge/compileSdkVersion-28-brightgreen.svg alt="compileSdkVersion 28" data-canonical-src="https://img.shields.io/badge/compileSdkVersion-27-yellow.svg?style=true" style="max-width:100%;">

## Navigation Drawer In Action
![ezgif com-resize](https://user-images.githubusercontent.com/39665412/50374360-dba49b80-0627-11e9-9047-52cb7a78b592.gif)

## Tutorial

There are many other tutorials/guide online that suggests using widgets like `RecycerView`, `ListView` or `Spinners`. However they are quite complicated and I suggest an easier solution that involves creating two seperate `menu` and having the 'spinner' arrow as a simple `ImageView`.
### Layout
We will be using a `selector` to control the behaviour of the imageview and navigation drawer menu. First, create the `selector` in xml.

```xml
<?xml version="1.0" encoding="utf-8"?>

<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@drawable/ic_arrow_drop_up_white_24dp"
        android:state_selected="true" />
    <item android:drawable="@drawable/ic_arrow_drop_down_white_24dp" />
</selector>
```
In the navigation drawer [header layout](https://github.com/wRorsjakz/Android-GmailNavigationDrawer/blob/master/app/src/main/res/layout/nav_header_layout.xml), include a `ImageView` with its src as the `selector` drawable declared above.

```xml
<ImageView
        android:id="@+id/nav_header_arrow"
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_weight="20"
        android:padding="2dp"
        android:src="@drawable/navdrawer_menu_toggle" />
```

To create the menus, create two seperate navigation drawer `menu` like how you would normally do so for a standard navigation drawer.

To have menu items grouped together with a seperator on top the group and without a title, just give the group an id:
```xml
 <group
        android:id="@+id/menu_bottom_group">

        <!-- Add menu items here -->
</group>
```
To add the counter of the menu items, create a seperate layout xml file with a `TextView`.
```xml
<TextView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="wrap_content"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical"
    android:textAppearance="@style/TextAppearance.AppCompat.Body2">
</TextView>
```
In the `menu` xml file, add the `TextView` to the menu item using `app:actionLayout`.
```xml
app:actionLayout="@layout/menu_counter_layout"
```
### Java
In order to respond user's click, attach a `setOnClickListener()` to the arrow `ImageView` in the navigation drawer header after intialising it using `getHeaderView()`:

```java
private ImageView imageViewArrow;

// Intialise the ImageView
View header = navigationView.getHeaderView(0);
imageViewArrow = header.findViewById(R.id.nav_header_arrow);

<!--- Attach listener to imageViewArrow here -->

```

The `onClick()` responds differently depending on which menu is currently showing - the other menu is thus shown. The easiest method I found is to use a `selector` for the imageview as declared above.

In java code, check for the state of the imageview with `isSelected()`. If you want your previously checked menu item to remain checked after returning from the other menu, then store the previous menu item's id and use `setCheckedItem()`.

```java
imageViewArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!v.isSelected()){
                   currentItem = navigationView.getCheckedItem().getItemId();
                   navigationView.getMenu().clear();
                   navigationView.inflateMenu(R.menu.nav_menu_two);
                   v.setSelected(true);
               }
               else{
                   navigationView.getMenu().clear();
                   navigationView.inflateMenu(R.menu.nav_menu_one);
                   navigationView.setCheckedItem(currentItem);
                   v.setSelected(false);
               }
            }
        });
```

Use `getMenu().clear()` to remove the current menu in the drawer and `inflateMenu()` to inflate the other menu to by shown.

Respond to menu item click events in both menus as per normal be overriding `onNavigationItemSelected()`.

#### Unread messages counter
![gmaillappscreenshot](https://user-images.githubusercontent.com/39665412/50374153-c1b58980-0624-11e9-91e0-a308c349aa92.jpg)

To display number of unread messages in the menu items, I created a method called `displayCounter(int menuItemId, int count)`. It iterates through all the menu items, and displays the integer `count` in the menu item whose id matches `menuItemId`.
```java
private void displayCounter(int menuItemId, int count){
        for (int i=0; i < navigationView.getMenu().size(); i++){
            MenuItem item = navigationView.getMenu().getItem(i);
            if (item.getItemId() == menuItemId){
                TextView counter = (TextView) item.getActionView();
                counter.setText(Integer.toString(count));
            }
        }
    }
```

#### Lifecycle

If you want your app to show a fragment as default when the app is first started, you can do so under launcher activity's `onCreate()`. However, this will cause your default fragment to be always shown when your activity is destroyed and recreated, such as after a configuration change. Hence under `onCreate()`, check if `savedInstanceState` is null first.

```java
if(savedInstanceState == null){
            navigationView.setCheckedItem(R.id.menu_primary);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout_id,
                    new PrimaryFragment()).commit();
            currentItem = R.id.menu_primary;
        }
```

## Dependencies / Built With 
- [Design Support Library](https://developer.android.com/reference/android/support/design/package-summary) - The Design package provides APIs to support adding material design components and patterns to your apps
- [CircleImageView](https://github.com/hdodenhof/CircleImageView) by hdodenhof - A circular ImageView for Android 
```java
dependencies {
  // Design Support Library
  implementation 'com.android.support:design:28.0.0'
  //CircleImageView
  implementation 'de.hdodenhof:circleimageview:2.2.0'
}
```
## License
```tex
MIT License

Copyright (c) 2018 Nicholas Gan

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```