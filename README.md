# cg16
Java + OpenGL

## description

- MODE 1  (STRAIGHT: default)<br>
マウスを押している間、自転車が X 軸上で前進と後退を繰り返します。

- MODE 2 (CIRCLE) <br>
マウスを押している間、自転車が反時計回りに円を描いて進みます。

- MODE 3 (LISSAJOUS)<br>
マウスを押している間、自転車がリサージュ曲線を描いて進みます。計算量が多いため、ダブルバッファリングをしていますが自転車がちらついて見えてしまいます。マウスを離した時自転車が消えて見えることがありますが、押し続けていると動いているのがわかります。また、自転車の回転の向きが大きいとき(Z 軸と平行に近づくとき)余分に回転してしまっています。<br>


* L キーで地面の模様が切り替えられます。地面の模様には2パターンあり、FLOWER と GUIDELINE です。GUIDELINE モードの曲線が自転車の軌跡です。<br>

## Various shapes
- 本当に初歩の話だけど、javaなのでいちいち作ってあげないといけない。
- Cylinder.<br>(c++): https://www21.atwiki.jp/opengl/pages/69.html
- `GLfloat`は`float`と等価、らしい。とりあえずこれでうまくいっているので大丈夫だろう。<br>http://www.wakayama-u.ac.jp/~wuhy/GSS/02.html
- glRotatef(回転角度,X軸の影響度,Y軸の影響度,Z軸の影響度)で回転行列を適用。<br>http://atelier-yoka.com/dev_android/p_main.php?file=apigl10glrotatef
- 回転が先か移動が先か<br>初期座標の設定なんて言うものは引数で与えない方が良いということか。<br>http://nn-hokuson.hatenablog.com/entry/2014/03/13/210733
