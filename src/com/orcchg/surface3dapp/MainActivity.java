/**
 * Copyright (c) 2015, Alov Maxim <alovmax@yandex.ru>
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.orcchg.surface3dapp;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import org.apache.commons.io.FilenameUtils;

import com.orcchg.surface3d.SceneInfo;
import com.orcchg.surface3d.Surface3DView;
import com.orcchg.surface3d.Surface3DView.DrawType;
import com.orcchg.surface3dapp.utils.OnFileSelectedListener;
import com.orcchg.surface3dapp.utils.OpenFileDialog;
import com.orcchg.surface3dapp.utils.Utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity {
  private static final String TAG = "Main";
  private static final String bundleKey_BackgroundColor = "bundleKey_BackgroundColor";
  private static final String bundleKey_DrawType = "bundleKey_DrawType";
  private static final String bundleKey_AxisVisible = "bundleKey_AxisVisible";
  private static final String bundleKey_LoadedFromType = "bundleKey_LoadedFromType";
  private static final String bundleKey_PreviousFragmentType = "bundleKey_PreviousFragmentType";
  private static final String bundleKey_CurrentFragmentType = "bundleKey_CurrentFragmentType";
  private static final String bundleKey_ModelName = "bundleKey_ModelName";
  private static final String bundleKey_AssetFolder = "bundleKey_AssetFolder";
  private static final String bundleKey_ModelFilePath = "bundleKey_ModelFilePath";

  private WeakReference<Surface3DView> mSurfaceRef;
  private Toast mToast;
  
  /* Globals */
  private int backgroundColor = -1;
  private int drawType = DrawType.MESH.getValue();
  private boolean axisVisible = false;
  private int loadedFrom = LoadedFromType.ASSETS.getValue();
  private int prevFragment = FragmentType.FOLDERS.getValue();
  private int currentFragment = FragmentType.FOLDERS.getValue();
  private String modelName = "";
  private String assetFolder = "";
  private String modelFilePath = "";
  private SceneInfo mLastSceneInfo = null;
  
  private static final String SCENEINFO_NAME_KEY     = "SCENEINFO_NAME_KEY";
  private static final String SCENEINFO_VERTICES_KEY = "SCENEINFO_VERTICES_KEY";
  private static final String SCENEINFO_POLYGONS_KEY = "SCENEINFO_POLYGONS_KEY";
  private static final String SCENEINFO_COLORS_KEY   = "SCENEINFO_COLORS_KEY";
  private static final String SCENEINFO_TEXTURES_KEY = "SCENEINFO_TEXTURES_KEY";
  private static final String SCENEINFO_MATERIALS_KEY = "SCENEINFO_MATERIALS_KEY";
  private static final String SCENEINFO_MESHES_KEY   = "SCENEINFO_MESHES_KEY";

  @SuppressLint("ShowToast")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Log.i(TAG, "onCreate: " + state());
    if (backgroundColor == -1) {
      backgroundColor = getResources().getIntArray(R.array.palette)[14];  // magenta
    }
    mToast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
    
    switch (getResources().getConfiguration().orientation) {
      case Configuration.ORIENTATION_PORTRAIT:
        mToast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 200);
        break;
      case Configuration.ORIENTATION_LANDSCAPE:
        mToast.setGravity(Gravity.TOP | Gravity.CENTER, -350, 200);
        break;
    }
    
    Surface3DView surface = (Surface3DView) findViewById(R.id.surface3d);
    mSurfaceRef = new WeakReference<Surface3DView>(surface);
    
    GridView grid = (GridView) findViewById(R.id.gridView_onDrawer);
    grid.setAdapter(new GridBoxAdapter(getApplicationContext()));
//    grid.setOnItemClickListener(new OnItemClickListener() {
//      @Override
//      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        backgroundColor = (int) parent.getItemAtPosition(position);
//        surface.setBackgroundColor(backgroundColor);
//      }});
    grid.setOnItemClickListener(new GridItemClickListener(this, surface));
    
//    surface.setModelLoadedListener(new Surface3DView.ModelLoadedListener() {
//      @Override
//      public void onLoaded(SceneInfo scene_info) {
//        mLastSceneInfo = scene_info;
//        mLastSceneInfo.name = modelName;
//        if (FragmentType.fromInt(currentFragment) == FragmentType.INFO) {
//          runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//              setInfoFragment(
//                  modelName,
//                  mLastSceneInfo.name,
//                  mLastSceneInfo.vertices,
//                  mLastSceneInfo.polygons,
//                  mLastSceneInfo.colors,
//                  mLastSceneInfo.textures,
//                  mLastSceneInfo.materials,
//                  mLastSceneInfo.meshes);
//            }});
//        }
//      }
//      @Override
//      public void onFailed(String message) {
//        showToast(message);
//      }
//    });
    surface.setModelLoadedListener(new ModelLoadedListener(this));
    
//    surface.setNativeEventListener(new Surface3DView.NativeEventListener() {
//      @Override
//      public void onEvent(String message) {
//        showToast(message);
//      }
//    });
    surface.setNativeEventListener(new NativeEventListener(this));

    clearInfo();  // avoid NPE
    if (savedInstanceState != null) {
      restoreState(savedInstanceState);
    }
  }
  
  @Override
  public void onPause() {
    super.onPause();
    Log.i(TAG, "onPause: " + state());
  }
  
  @Override
  public void onResume() {
    super.onResume();
    Log.i(TAG, "onResume: " + state());
    switch (FragmentType.fromInt(currentFragment)) {
      case FOLDERS:
        setListFragment();
        break;
      case MODELS:
        setListButtonFragment(assetFolder, getFormatIdByString(assetFolder));
        break;
      case INFO:
        setInfoFragment(
            modelName,
            mLastSceneInfo.name,
            mLastSceneInfo.vertices,
            mLastSceneInfo.polygons,
            mLastSceneInfo.colors,
            mLastSceneInfo.textures,
            mLastSceneInfo.materials,
            mLastSceneInfo.meshes);
        break;
    }
    
    Surface3DView surface = mSurfaceRef.get();
    if (surface != null) {
      surface.setBackgroundColor(backgroundColor);
      surface.setDrawType(DrawType.fromInt(drawType));
      surface.showAxis(axisVisible);
      switch (LoadedFromType.fromInt(loadedFrom)) {
        case ASSETS:
          if (assetFolder.isEmpty() || assetFolder == null || modelName.isEmpty() || modelName == null) {
            return;
          }
          loadAsset(assetFolder, modelName);
          break;
        case FILESYSTEM:
          if (modelFilePath.isEmpty() || modelFilePath == null || !((new File(modelFilePath)).exists())) {
            return;
          }
          File directory = new File(modelFilePath).getParentFile();
          String[] materialFilepaths = Utility.listFiles(directory, getResources().getStringArray(R.array.textures_filetypes), getResources().getStringArray(R.array.materials_filetypes));
          surface.loadResource(modelFilePath, materialFilepaths);
          break;
        default:
          return;
      }
    }
  }
  
  @Override
  public void onStop() {
    super.onStop();
    Log.i(TAG, "onStop: " + state());
  }
  
  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.i(TAG, "onDestroy: " + state());
    Surface3DView surface = mSurfaceRef.get();
    if (surface != null) {
      surface.recycle();
      surface = null;
    }
    mToast = null;
    System.gc();
  }
  
  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
//    switch (newConfig.orientation) {
//      case Configuration.ORIENTATION_PORTRAIT:
//        mToast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 200);
//        break;
//      case Configuration.ORIENTATION_LANDSCAPE:
//        mToast.setGravity(Gravity.TOP | Gravity.CENTER, -350, 200);
//        break;
//    }
  }
  
  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    savedInstanceState.putInt(bundleKey_BackgroundColor, backgroundColor);
    savedInstanceState.putInt(bundleKey_DrawType, drawType);
    savedInstanceState.putBoolean(bundleKey_AxisVisible, axisVisible);
    savedInstanceState.putInt(bundleKey_LoadedFromType, loadedFrom);
    savedInstanceState.putInt(bundleKey_PreviousFragmentType, prevFragment);
    savedInstanceState.putInt(bundleKey_CurrentFragmentType, currentFragment);
    savedInstanceState.putString(bundleKey_ModelName, modelName);
    savedInstanceState.putString(bundleKey_AssetFolder, assetFolder);
    savedInstanceState.putString(bundleKey_ModelFilePath, modelFilePath);
    
    savedInstanceState.putString(SCENEINFO_NAME_KEY, mLastSceneInfo.name);
    savedInstanceState.putInt(SCENEINFO_VERTICES_KEY, mLastSceneInfo.vertices);
    savedInstanceState.putInt(SCENEINFO_POLYGONS_KEY, mLastSceneInfo.polygons);
    savedInstanceState.putInt(SCENEINFO_COLORS_KEY, mLastSceneInfo.colors);
    savedInstanceState.putInt(SCENEINFO_TEXTURES_KEY, mLastSceneInfo.textures);
    savedInstanceState.putInt(SCENEINFO_MATERIALS_KEY, mLastSceneInfo.materials);
    savedInstanceState.putInt(SCENEINFO_MESHES_KEY, mLastSceneInfo.meshes);
    super.onSaveInstanceState(savedInstanceState);
  }
  
  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    restoreState(savedInstanceState);
  };

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }
  
  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    if (axisVisible) {
      menu.getItem(6).setTitle(R.string.action_hideAxis);
    } else {
      menu.getItem(6).setTitle(R.string.action_showAxis);
    }
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    final Surface3DView surface = mSurfaceRef.get();
    if (surface != null) {
      switch (id) {
        case R.id.action_importFromFS:
          OpenFileDialog dialogf =
              new OpenFileDialog(
                  this,
                  Environment.getExternalStorageDirectory().getPath(),
                  getResources().getStringArray(R.array.models_filetypes),
                  new OnFileSelectedListener() {
                    @Override
                    public void onFileSelected(File f) {
                      modelName = f.getName();
                      loadedFrom = LoadedFromType.FILESYSTEM.getValue();
                      modelFilePath = f.getAbsolutePath();
                      String[] materialFilepaths = Utility.listFiles(f.getParent(), getResources().getStringArray(R.array.textures_filetypes), getResources().getStringArray(R.array.materials_filetypes));
                      surface.loadResource(modelFilePath, materialFilepaths);
                    }});
          dialogf.show();
          break;
        case R.id.action_pointCloud:
          surface.setDrawType(DrawType.POINT_CLOUD);
          drawType = DrawType.POINT_CLOUD.getValue();
          break;
        case R.id.action_wireframe:
          surface.setDrawType(DrawType.WIREFRAME);
          drawType = DrawType.WIREFRAME.getValue();
          break;
        case R.id.action_mesh:
          surface.setDrawType(DrawType.MESH);
          drawType = DrawType.MESH.getValue();
          break;
        case R.id.action_drop:
          surface.dropState();
          break;
        case R.id.action_clear:
          surface.clear();
          clearInfo();
          break;
        case R.id.action_showAxis:
          surface.showAxis(!axisVisible);
          axisVisible = !axisVisible;
          break;
        case R.id.action_info:
          setInfoFragment(
              modelName,
              mLastSceneInfo.name,
              mLastSceneInfo.vertices,
              mLastSceneInfo.polygons,
              mLastSceneInfo.colors,
              mLastSceneInfo.textures,
              mLastSceneInfo.materials,
              mLastSceneInfo.meshes);
          break;
      }
    }
    return super.onOptionsItemSelected(item);
  }
  
  /* Set active fragment */
  // --------------------------------------------------------------------------
  void setListFragment() {
    currentFragment = FragmentType.FOLDERS.getValue();
    ListFragment fragment = ListFragment.newInstance();
    replace(fragment);
  }
  
  void setListButtonFragment(String title, int formatId) {
    currentFragment = FragmentType.MODELS.getValue();
    ListButtonFragment fragment = ListButtonFragment.newInstance(title, formatId);
    replace(fragment);
  }
  
  void setInfoFragment(
      String title,
      String name,
      int vertices,
      int polygons,
      int colors,
      int textures,
      int materials,
      int meshes) {
    
    currentFragment = FragmentType.INFO.getValue();
    InfoFragment fragment = InfoFragment.newInstance(title, name, vertices, polygons, colors, textures, materials, meshes);
    replace(fragment);
  }
  
  private void replace(Fragment fragment) {
    FragmentManager fm = getFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();
    ft.replace(R.id.container_view, fragment);
    ft.commit();
    fm.executePendingTransactions();
  }
  
  /* Internal methods */
  // --------------------------------------------------------------------------
  void loadAsset(String folder, String resource) {
    StringBuilder prefix = new StringBuilder("bsdmodels/").append(folder).append("/");
    StringBuilder materialsFolder = new StringBuilder(prefix.toString()).append(FilenameUtils.removeExtension(resource));
    String[] materials = null;
    try {
      materials = getAssets().list(materialsFolder.toString());
    } catch (IOException e) {
      Log.e(TAG, "Exception during listing assets");
      e.printStackTrace();
    }
    materialsFolder.append("/");
    for (int imt = 0; imt < materials.length; ++imt) {
      Log.d(TAG, "Found material / texture: " + materials[imt]);
      materials[imt] = materialsFolder.toString() + materials[imt];
    }
    modelName = resource;
    loadedFrom = LoadedFromType.ASSETS.getValue();
    String modelFilePath = prefix.toString() + resource;
    String[] materialResources = new String[materials.length];
    System.arraycopy(materials, 0, materialResources, 0, materials.length);
    
    Surface3DView surface = mSurfaceRef.get();
    if (surface != null) {
      surface.loadResource(getAssets(), modelFilePath, materials);
    }
  }
  
  FragmentType getPrevFragmentType() {
    return FragmentType.fromInt(prevFragment);
  }
  
  String getModelName() {
    return modelName;
  }
  
  String getAssetFolder() {
    return assetFolder;
  }
  
  SceneInfo getLastSceneInfo() {
    return mLastSceneInfo;
  }
  
  int getCurrentFragment() {
    return currentFragment;
  }
  
  void setAssetFolder(String folder) {
    assetFolder = folder;
  }
  
  void setLastSceneInfo(SceneInfo info, String name) {
    mLastSceneInfo = info;
    mLastSceneInfo.name = name;
  }
  
  void setPrevFragmentType(FragmentType type) {
    prevFragment = type.getValue();
  }
  
  void setBgColor(int bgColor) {
    backgroundColor = bgColor;
  }
  
  int getFormatIdByString(String id) {
    if (id.equals(getResources().getString(R.string.model_folder_3D))) { return R.string.model_folder_3D; }
    if (id.equals(getResources().getString(R.string.model_folder_3DS))) { return R.string.model_folder_3DS; }
    if (id.equals(getResources().getString(R.string.model_folder_AC))) { return R.string.model_folder_AC; }
    if (id.equals(getResources().getString(R.string.model_folder_ASE))) { return R.string.model_folder_ASE; }
    if (id.equals(getResources().getString(R.string.model_folder_B3D))) { return R.string.model_folder_B3D; }
    if (id.equals(getResources().getString(R.string.model_folder_BLEND))) { return R.string.model_folder_BLEND; }
    if (id.equals(getResources().getString(R.string.model_folder_BVH))) { return R.string.model_folder_BVH; }
    if (id.equals(getResources().getString(R.string.model_folder_COB))) { return R.string.model_folder_COB; }
    if (id.equals(getResources().getString(R.string.model_folder_Collada))) { return R.string.model_folder_Collada; }
    if (id.equals(getResources().getString(R.string.model_folder_CSM))) { return R.string.model_folder_CSM; }
    if (id.equals(getResources().getString(R.string.model_folder_DXF))) { return R.string.model_folder_DXF; }
    if (id.equals(getResources().getString(R.string.model_folder_FurnitureOBJ))) { return R.string.model_folder_FurnitureOBJ; }
    if (id.equals(getResources().getString(R.string.model_folder_HMP))) { return R.string.model_folder_HMP; }
    if (id.equals(getResources().getString(R.string.model_folder_IFC))) { return R.string.model_folder_IFC; }
    if (id.equals(getResources().getString(R.string.model_folder_IRR))) { return R.string.model_folder_IRR; }
    if (id.equals(getResources().getString(R.string.model_folder_IRRMesh))) { return R.string.model_folder_IRRMesh; }
    if (id.equals(getResources().getString(R.string.model_folder_LWO))) { return R.string.model_folder_LWO; }
    if (id.equals(getResources().getString(R.string.model_folder_LWS))) { return R.string.model_folder_LWS; }
    if (id.equals(getResources().getString(R.string.model_folder_MD2))) { return R.string.model_folder_MD2; }
    if (id.equals(getResources().getString(R.string.model_folder_MD5))) { return R.string.model_folder_MD5; }
    if (id.equals(getResources().getString(R.string.model_folder_MDL))) { return R.string.model_folder_MDL; }
    if (id.equals(getResources().getString(R.string.model_folder_MS3D))) { return R.string.model_folder_MS3D; }
    if (id.equals(getResources().getString(R.string.model_folder_NFF))) { return R.string.model_folder_NFF; }
    if (id.equals(getResources().getString(R.string.model_folder_OBJ))) { return R.string.model_folder_OBJ; }
    if (id.equals(getResources().getString(R.string.model_folder_OFF))) { return R.string.model_folder_OFF; }
    if (id.equals(getResources().getString(R.string.model_folder_Ogre))) { return R.string.model_folder_Ogre; }
    if (id.equals(getResources().getString(R.string.model_folder_PLY))) { return R.string.model_folder_PLY; }
    if (id.equals(getResources().getString(R.string.model_folder_Q3D))) { return R.string.model_folder_Q3D; }
    if (id.equals(getResources().getString(R.string.model_folder_RAW))) { return R.string.model_folder_RAW; }
    if (id.equals(getResources().getString(R.string.model_folder_SMD))) { return R.string.model_folder_SMD; }
    if (id.equals(getResources().getString(R.string.model_folder_STL))) { return R.string.model_folder_STL; }
    if (id.equals(getResources().getString(R.string.model_folder_TER))) { return R.string.model_folder_TER; }
    if (id.equals(getResources().getString(R.string.model_folder_WRL))) { return R.string.model_folder_WRL; }
    if (id.equals(getResources().getString(R.string.model_folder_X))) { return R.string.model_folder_X; }
    if (id.equals(getResources().getString(R.string.model_folder_XGL))) { return R.string.model_folder_XGL; }
    return -1;
  }
  
  /* Private methods */
  // --------------------------------------------------------------------------
  private void clearInfo() {
    modelName = "";
    mLastSceneInfo = new SceneInfo();
    if (FragmentType.fromInt(currentFragment) == FragmentType.INFO) {
      setInfoFragment(
          modelName,
          mLastSceneInfo.name,
          mLastSceneInfo.vertices,
          mLastSceneInfo.polygons,
          mLastSceneInfo.colors,
          mLastSceneInfo.textures,
          mLastSceneInfo.materials,
          mLastSceneInfo.meshes);
    }
  }
  
  private void showToast(final String message) {
    mToast.setText(message);
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        mToast.show();
      }});
  }
  
  private void restoreState(Bundle savedInstanceState) {
    backgroundColor = savedInstanceState.getInt(bundleKey_BackgroundColor);
    drawType = savedInstanceState.getInt(bundleKey_DrawType);
    axisVisible = savedInstanceState.getBoolean(bundleKey_AxisVisible);
    loadedFrom = savedInstanceState.getInt(bundleKey_LoadedFromType);
    prevFragment = savedInstanceState.getInt(bundleKey_PreviousFragmentType);
    currentFragment = savedInstanceState.getInt(bundleKey_CurrentFragmentType);
    modelName = savedInstanceState.getString(bundleKey_ModelName);
    assetFolder = savedInstanceState.getString(bundleKey_AssetFolder);
    modelFilePath = savedInstanceState.getString(bundleKey_ModelFilePath);
    
    mLastSceneInfo = new SceneInfo(
        savedInstanceState.getInt(SCENEINFO_VERTICES_KEY),
        savedInstanceState.getInt(SCENEINFO_POLYGONS_KEY),
        savedInstanceState.getInt(SCENEINFO_COLORS_KEY),
        savedInstanceState.getInt(SCENEINFO_TEXTURES_KEY),
        savedInstanceState.getInt(SCENEINFO_MATERIALS_KEY),
        savedInstanceState.getInt(SCENEINFO_MESHES_KEY));
    mLastSceneInfo.name = savedInstanceState.getString(SCENEINFO_NAME_KEY);
    Log.d(TAG, "Scene info: " + mLastSceneInfo.toString());
  }
  
  private String state() {
    StringBuilder builder = new StringBuilder("State");
    builder.append(": ModelName=").append(modelName)
           .append(", AssetFolder=").append(assetFolder)
           .append(", ModelFilePath=").append(modelFilePath)
           .append(", DrawType=").append(drawType)
           .append(", LoadedFromType=").append(loadedFrom)
           .append(", PreviousFragmentType=").append(prevFragment)
           .append(", CurrentFragmentType=").append(currentFragment)
           .append(", Background=").append(String.format("#%08X", (0xFFFFFFFF & backgroundColor)));
    return builder.toString();
  }
  
  /* Interface implementation */
  // --------------------------------------------------------------------------
  private static final class GridItemClickListener implements OnItemClickListener {
    private final WeakReference<MainActivity> activityRef;
    private final WeakReference<Surface3DView> surfaceRef;
    
    private GridItemClickListener(MainActivity activity, Surface3DView surface) {
      activityRef = new WeakReference<MainActivity>(activity);
      surfaceRef = new WeakReference<Surface3DView>(surface);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      int backgroundColor = (int) parent.getItemAtPosition(position);
      final MainActivity activity = activityRef.get();
      if (activity != null) {
        activity.setBgColor(backgroundColor);
      }
      final Surface3DView surface = surfaceRef.get();
      if (surface != null) {
        surface.setBackgroundColor(backgroundColor);
      }
    }
  }
  
  private static final class ModelLoadedListener implements Surface3DView.ModelLoadedListener {
    private final WeakReference<MainActivity> activityRef;
    
    private ModelLoadedListener(MainActivity activity) {
      activityRef = new WeakReference<MainActivity>(activity);
    }
    
    @Override
    public void onLoaded(SceneInfo scene_info) {
      final MainActivity activity = activityRef.get();
      if (activity != null) {
        activity.setLastSceneInfo(scene_info, activity.getModelName());
        if (FragmentType.fromInt(activity.getCurrentFragment()) == FragmentType.INFO) {
          activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
              activity.setInfoFragment(
                  activity.getModelName(),
                  activity.getLastSceneInfo().name,
                  activity.getLastSceneInfo().vertices,
                  activity.getLastSceneInfo().polygons,
                  activity.getLastSceneInfo().colors,
                  activity.getLastSceneInfo().textures,
                  activity.getLastSceneInfo().materials,
                  activity.getLastSceneInfo().meshes);
            }});
        }
      }
    }

    @Override
    public void onFailed(String message) {
      final MainActivity activity = activityRef.get();
      if (activity != null) {
        activity.showToast(message);
      }
    }
  }
  
  private static final class NativeEventListener implements Surface3DView.NativeEventListener {
    private final WeakReference<MainActivity> activityRef;
    
    private NativeEventListener(MainActivity activity) {
      activityRef = new WeakReference<MainActivity>(activity);
    }

    @Override
    public void onEvent(String message) {
      final MainActivity activity = activityRef.get();
      if (activity != null) {
        activity.showToast(message);
      }
    }
  }
}
