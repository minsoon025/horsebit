# 시작하기  

## gradle 파일 편집  
[Gradle Scripts] -> [build.gradle] 에 다래와 같은 코드를 추가  
```kotlin
android {

   // 생략

    buildFeatures {
        viewBinding true
    }
}
```

그 후 Sync Now 링크를 클릭하여 싱크  

## 공통된 엑티비티의 과정  
모든 엑티비티에 대하여 아래와 같은 기본 틀을 사용  

```kotlin
class [엑티비티이름] : AppCompatActivity() {

    private val binding by lazy {
        [엑티비티이름]Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.[뷰 이름].[동작]
    }

}
```

주의 할 점: [엑티비티이름]이 "MainActivity"라면..  
class MainActivity : AppCompatActivity() { 와 같이 쓰고,  
ActivityMainBinding.inflate(layoutInflater) 와 같이 쓴다.  

## 화면이동 및 데이터 전달  
보내는 쪽  (MainActivity)  
```kotlin
val intent = Intent(this, SubActivity::class.java)  // [이동할 엑티비티 이름]::class.java
intent.putExtra("key", "value") // intent.putExtra("[키]", "[값]")
startActivity(intent)
// finish() -> 엑티비티 파괴
```

받는 쪽 (SubActivity)  
```kotlin
if(intent.hasExtra("key")) {    // intent에 [키]의 값이 있다면 
    binding.[뷰 이름].text = intent.getStringExtra("key")
}
```