# RollingNumber

### TODO
- [ ] 숫자 뿐만 아니라 여러 텍스트도 가능하게끔 만들기.
- [ ] README 작성 마무리 하기.

### RollingNumber?
가격 정보를 보여줄 때 변경되는 애니메이션을 보여주기 위한 Custom View.

### Usage
```xml
<!-- in your theme xml -->
<item name="rollingNumberStyle">@style/RollingNumberStyle</item>
```
```kt
// in your activity
rollingNumber.text = "1,000,000" // text is separated by rest letter
```

### Customize
RollingNumber 는 TextView 를 상속받아 만든 CustomView 가 아니기 때문에 커스터마이즈 할 수 있는게 많지 않다.
```kt
rollingNubmer.text
rollingNumber.setTextColor
rollingNumber.setTextStyle
rollingNubmer.setDuration
rollingNumber.textSize
```

```
android:text=""
android:textSize=""
android:textColor=""
android:textStyle=""
android:duration=""
```