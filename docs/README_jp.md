# Nearby Places for Android documentation

<!-- MDTOC maxdepth:6 firsth1:0 numbering:0 flatten:0 bullets:1 updateOnSave:1 -->

- [説明](#説明)   
- [近くの場所を特定](#近くの場所を特定)   
   - [デバイスの位置情報(#デバイスの位置情報)   
   - [ジオコーディング](#ジオコーディング)   
   - [方位と距離の計算](#方位と距離の計算)   
- [地図上に場所を表示](#地図上に場所を表示)   
   - [視点の設定](#視点の設定)   
   - [位置情報の表示](#位置情報の表示)   
   - [新しい検索結果で地図表示を更新](#新しい検索結果で地図表示を更新する)   
- [ルート案内](#ルート案内)   

<!-- /MDTOC -->
---

## 説明

Esri の [World Geocoding Service](https://developers.arcgis.com/features/geocoding/) または独自の[カスタム ジオコーディング サービス](http://desktop.arcgis.com/en/arcmap/latest/manage-data/geocoding/the-geocoding-workflow.htm)を使用して、周囲の場所の探索をカスタマイズできます。このアプリでは、Esri のジオコーディング サービスを使用して、デバイスの現在地からデフォルトの半径内にあるホテル、レストラン、またはバーを検索し、選択した関心のある場所への経路案内を提供します。ArcGIS Android Geometry Engine API を使用して、デバイスの位置からの距離と方位に基づいて取得した場所のリストをソートします。

このサンプル アプリケーションはオープン ソースで、GitHub で公開されています。このサンプル アプリケーションを変更して、興味のある場所の[カテゴリ](https://developers.arcgis.com/rest/geocode/api-reference/geocoding-category-filtering.htm)の表示や、独自のカスタム ロケーターを[構成](http://desktop.arcgis.com/en/arcmap/latest/manage-data/geocoding/creating-an-address-locator.htm)することができます。

## 近くの場所を特定

### デバイスの位置情報

Nearby Places は、マップレス アプリのパターンを採用しており、まず近くの場所のリストを表示します。アプリは地図ではなくリストで始まるので、デバイスの位置情報は Google の Location Services API を使って取得します。この後、Runtime SDK を使用して、`MapView` の外にあるデバイスの位置情報を取得することができます。デバイスの位置情報を取得する前には、アプリはデバイスの GPS と無線の設定がオンになっていることを確認し、Google のロケーションサービスを設定します。

```java
// Google's location services are configured in the
// PlacesFragment onCreate method.
if (mGoogleApiClient == null) {
    // Create an instance of GoogleAPIClient.
    mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
      .addConnectionCallbacks(this)
      .addOnConnectionFailedListener(this)
      .addApi(LocationServices.API)
      .build();
}
// Once Google's location service is connected,
// you can use the device location to start the
// geocoding search.
@Override public void onConnected(@Nullable Bundle bundle) {
    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    startSearch(mLastLocation);
}
```

位置を決めることで場所を探すことができます。

### ジオコーディング

場所の検索では、カテゴリ フィルター(例:「Hotel」、「Food」、「Pizza」)を使用して、現在のデバイスの位置の近くにあるこれらのタイプに一致する場所を見つけます。World Geocoding サービスでは、カテゴリの階層構造を使用しており、カテゴリ レベル1の記述子(例:「POI」(place of interest))のような高レベルの概念を検索したり、「Brazilian Food」や「Science Museum」のようなより具体的なカテゴリ レベル3のタイプを使用することができます。カテゴリ フィルターやその他の検索条件は、SDK の [Geocode Parameters](https://developers.arcgis.com/android/latest/api-reference/reference/com/esri/arcgisruntime/tasks/geocode/GeocodeParameters.html) を用いて定義します。

ジオコード パラメーターには、返す結果の最大数、希望するカテゴリ、デバイスの現在地、[出力フィールド](https://developers.arcgis.com/rest/geocode/api-reference/geocoding-service-output.htm#ESRI_SECTION1_42D7D3D0231241E9B656C01438209440)を以下のパターンで設定します。なお、ジオコーディングサービスは、[こちらで定義された](https://developers.arcgis.com/rest/geocode/api-reference/geocoding-category-filtering.htm#ESRI_SECTION1_502B3FE2028145D7B189C25B1A00E17B)特定のカテゴリのリストをサポートしています。Nearby Places では、現在地から半径50km以内にある検索結果の上位10件を対象としています。

```java
GeocodeParameters parameters = new GeocodeParameters();

// We're interested in the top ten nearest places
parameters.setMaxResults(10);

// This limits the search to a radius of 50 kilometers
// around the current location.
parameters.setPreferredSearchLocation(mCurrentLocation);

// Retrieve a mutable list
List categories = parameters.getCategories();

// Add a subset of specific keyword categories
// known to the service. See reference above.
categories.add("Food");
categories.add("Hotel");
categories.add("Pizza");
categories.add("Coffee Shop");
categories.add("Bar or Pub");

List outputAttributes = parameters.getResultAttributeNames();
// Return all of the service output fields
outputAttributes.add("*");

// Execute the search[]
<ListenableFuture> results = mLocatorTask.geocodeAsync(searchText, parameters);
```

取得された場所ごとに、端末の位置からの距離と方位が計算されます。

### 方位と距離の計算

距離と方位の決定には、[ジオメトリ エンジン](https://developers.arcgis.com/android/latest/api-reference/reference/com/esri/arcgisruntime/geometry/GeometryEngine.html)を使用して、デバイスの位置と近くの興味のある場所との間の[測地線の距離](https://geonet.esri.com/groups/coordinate-reference-systems/blog/2014/09/01/geodetic-distances-how-long-is-that-line-again)を計算します。距離の測定、空間的な関係の決定、ジオメトリの変更は、モバイル クライアント上のローカルで行うことができます。

現在のデバイスの位置情報を地理空間の計算に使用する場合は、空間参照を定義する必要があります。その理由として、Google のロケーション サービスが返す位置情報は、空間参照を指定していないためです。

```java
LinearUnit linearUnit = new LinearUnit(LinearUnitId.METERS);
AngularUnit angularUnit = new AngularUnit(AngularUnitId.DEGREES);

//The current location is obtained from the Google Location API
//needs to be created as a new point with a spatial reference.

// Get the spatial reference from the place returned from the geocoding service
SpatialReference spatialReference = place.getLocation().getSpatialReference() ;

// Create a new Point and use it for spatial calculations
Point newPoint = new Point(mCurrentLocation.getX(), mCurrentLocation.getY(), spatialReference );
GeodesicDistanceResult result =GeometryEngine.distanceGeodesic(newPoint, place.getLocation(),linearUnit, angularUnit, GeodeticCurveType.GEODESIC);
double distance = result.getDistance();
place.setDistance(Math.round(distance));

// Bearing degrees are returned in a range between -180 to 180.
double degrees = result.getAzimuth1();
if (degrees > -22.5  && degrees <= 22.5){
    bearing = "N";
}else if (degrees > 22.5 && degrees <= 67.5){
    bearing = "NE";
}else if (degrees > 67.5 && degrees <= 112.5){
    bearing = "E";
}else if (degrees > 112.5 && degrees <= 157.5){
    bearing = "SE";
}else if( (degrees > 157.5 ) || (degrees <= -157.5)){
    bearing = "S";
}else if (degrees > -157.5 && degrees <= -112.5){
    bearing = "SW";
}else if (degrees > -112.5 && degrees <= -67.5){
    bearing = "W";
}else if (degrees > -67.5 && degrees <= -22.5){
    bearing = "NW";
}
```

方位と距離が割り当てられた場所は、リスト ビューに表示されます。地図アイコンをクリックすると、近くの場所がマップ ビューに表示されます。

|List View|Map View|
|---|---|
|![List View](./images/nearby_list_view.png)|![Map View](./images/nearby_map_view.png)|


## 地図上に場所を表示

### 視点の設定

ユーザーが地図アイコンをクリックして地図を表示すると、リスト ビューに表示されている場所に基づいて地図の視点が生成されます。これは、検出された場所を繰り返し処理して `Multipoint` オブジェクトを作成し、Geometry Engine の buffer メソッドを使用して `Polygon` を生成することで実現しています。この `Polygon` から、空間参照を持つ長方形の領域が得られ、MapView に渡されます。

```java
List<Point> points = new ArrayList<>();

// Create an array of Point objects based on place locations
for ( Place place : places){
     points.add(place.getLocation());
}
// Create a Multipoint object and then buffer it to create a polygon
Multipoint mp = new Multipoint(points);
Polygon polygon = GeometryEngine.buffer(mp, 0.0007);

// Get the envelope from the polygon
Envelope viewpoint = polygon.getExtent();
```

### 場所の表示

MapView ができたら、`Viewpoint` を設定し、SDK の `LocationDisplay` を利用することができます。望ましい動作は、`MapView` が可視領域を現在の範囲に変更し、デバイスの位置情報を表示することです。現在、`Viewpoint` を使って位置情報を表示するためには、`MapView` の描画が終了するのを待つ必要があります。これは以下の手順で実現しています。

```java
mMapView = (MapView) root.findViewById(R.id.map);
ArcGISMap map = new ArcGISMap(Basemap.createNavigationVector());
mMapView.setMap(map);

// Set view point first
mMapView.setViewpoint(mViewpoint);

// Wait for draw status to be complete before getting and
// starting location display.
mMapView.addDrawStatusChangedListener(new DrawStatusChangedListener() {
   @Override public void drawStatusChanged(final DrawStatusChangedEvent drawStatusChangedEvent) {
        if (drawStatusChangedEvent.getDrawStatus() == DrawStatus.COMPLETED){
          mLocationDisplay = mMapView.getLocationDisplay();
          mLocationDisplay.startAsync();
        }
    }
});
```

### 新しい検索結果で地図表示を更新する

ユーザーが地図をタップしたり地図を移動したりすると、アプリはタップされた場所の詳細を表示するか、`SnackBar` が表示され、地図を移動した場所の新しい検索が開始されます。ナビゲーションの変更は、`NavigationChangedListener` を `MapView` に付随することによってモニターでチェックされます。イベントを受信するたびに、`MapView.isNavigating()` メソッドをチェックしますが、ユーザーがフリングジェスチャーを使用した場合、フリングの前にわずかな間があり、APIが現在遅延を考慮していないため、メソッドが false を返してしまいます。この問題を回避するために、メッセージキューにロジックを追加し、50ミリ秒後に実行することで、[フリング ジェスチャー](https://developer.android.com/training/gestures/detector.html) が確実にキャプチャされるようにしています。

```java
// This is a workaround for detecting when a fling motion has completed on the map view. The
// NavigationChangedListener listens for navigation changes, not whether navigation has completed. We wait
// a small interval before checking if map is view still navigating.

mNavigationChangedListener = new NavigationChangedListener() {

  @Override public void navigationChanged(final NavigationChangedEvent navigationChangedEvent) {
   if (!mMapView.isNavigating()){
     Handler handler = new Handler();
     handler.postDelayed(new Runnable() {

       @Override public void run() {

         if (!mMapView.isNavigating()) {
           onMapViewChange();
         }
       }
     }, 50);
   }
  }
};
mMapView.addNavigationChangedListener(mNavigationChangedListener);
```

マップ ビューから検索が開始されるたびに、GeometryEngine の within メソッドを使って、検索された場所がマップの現在の表現領域に制限されます。

```java
Envelope visibleMapArea = mapView.getVisibleArea().getExtent();

// Only show places within the visible map area
Point placePoint = new Point(location.getX(),location.getY(), SpatialReferences.getWgs84());
if (GeometryEngine.within(placePoint,visibleMapArea)){
  places.add(place);
}else{
  Log.i("GeometryEngine", "***Excluding " + place.getName() + " because it's outside the visible area of map.***");
}
```

## ルート案内

ツールバーの右上にあるルーティングの矢印をタップすると、その場所までの徒歩ルートが生成されます。

|Nearby Centered View|Route View|
|----|----|
|![Nearby Centered](./images/nearby_centered_place.png)|![Route View](./images/nearby_route_view.png)|

Nearby Places でのナビゲーションの表示は、[ArcGIS Online](http://doc.arcgis.com/en/arcgis-online/use-maps/get-directions.htm) と同様に [Runtime SDK](https://developers.arcgis.com/features/directions/) でも簡単に行うことができます。この設定方法の良い例は [Maps App](https://github.com/Esri/maps-app-android/blob/master/README.md) にあるので、ここでは説明しません。Maps App とは対照的に、Nearly Placesでは、ルーティング リクエストに移動モードと制限を使用して、徒歩ルートのリクエストを設定する方法を示しています。

```java

RouteParameters routeParameters = routeTaskFuture.get();

// Explicity set the travel mode attributes for a walking route
TravelMode mode = routeParameters.getTravelMode();
mode.setImpedanceAttributeName("WalkTime");
mode.setTimeAttributeName("WalkTime");

// Setting the restriction attributes for walk times
List<String> restrictionAttributes = mode.getRestrictionAttributeNames();
restrictionAttributes.clear();
restrictionAttributes.add("Avoid Roads Unsuitable for Pedestrians");
restrictionAttributes.add("Preferred for Pedestrians");
restrictionAttributes.add("Walking");

routeParameters.setTravelMode(mode);

ListenableFuture<RouteResult> routeResFuture = routeTask.solveRouteAsync(routeParameters);
```

属性リストの取得と設定は、SDK 全体に共通するパターンであり、さまざまな設定を制御するために変更可能なコレクションを使用しています。この例は、前述の「ジオコーディング」のセクションでも紹介しましたが、「Nearby Places」全体に共通しています。ルーティングの結果を表示するグラフィック オーバーレイを操作したり、地図にグラフィックを追加したりするのも、このパターンの一例です。

```java
// Showing the route result, the route overlay is added
// only once.  Subsequent access is via "getGraphcis"

if (mRouteOverlay == null) {
    mRouteOverlay = new GraphicsOverlay();
    mMapView.getGraphicsOverlays().add(mRouteOverlay);
}else{
    // Clear any previous route
    mRouteOverlay.getGraphics().clear();
}

//Adding graphics to the map
BitmapDrawable pin = (BitmapDrawable) ContextCompat.getDrawable(getActivity(),getDrawableForPlace(place)) ;
PictureMarkerSymbol pinSymbol = new PictureMarkerSymbol(pin);
Point graphicPoint = place.getLocation();
Graphic graphic = new Graphic(graphicPoint, pinSymbol);
mGraphicOverlay.getGraphics().add(graphic);
```
