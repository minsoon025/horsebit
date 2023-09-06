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
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.[뷰 이름].[동작]
    }

}
```