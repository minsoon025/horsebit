# 시작하기  

## gradle 파일 편집  
[Gradle Scripts] -> [build.gradle] 에 아래와 같은 코드를 추가  
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

## 이미지 추가  
[무료 이미지](https://www.flaticon.com/)  
[app] -> [res] -> [drawable]에 이미지를 추가  
```kotlin
binding.[이미지 뷰 이름].setImageResource(R.drawable.[이미지 이름])
```

## 토스트 메시지
```kotlin
Toast.makeText(this, "[내용]", Toast.LENGTH_SHORT).show()
```

## 리스트 뷰 (쉬움 버전)  
```kotlin
val [array 이름] : Array<String> = arrayOf("[item1]", "[item2]", "[item3]")
binding.[리스트 뷰 이름].adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, [array 이름])
```

## 리스트 뷰 (커스텀 버전)  
[app] -> [java] -> [com.[페키지이름].[프로젝트 이름]] -> [DataModels.kt] 에 class를 추가한다.  
```kotlin
data class [클래스 이름] (
    val [변수 이름]: [변수 타입],
)
```
[app] -> [res] -> [layout] 에 레이아웃을 추가한다. (파일명은 snake_case)  
[app] -> [java] -> [com.[페키지이름].[프로젝트 이름]]에 Adapter를 추가한다. (파일명은 PascalCase)  
아래와 같은 기본 틀을 같는다.  
```kotlin
class [어뎁터 이름](val context: Context, val [리스트 이름]: ArrayList<[리스트 클래스]>) : BaseAdapter() {

    override fun getCount(): Int {
        return [리스트 이름].size
    }

    override fun getItem(position: Int): Any {
        return [리스트 이름][position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val binding = [레이아웃 이름 PascalCase]Binding.inflate(LayoutInflater.from(context))
        val [클래스 변수 이름] = [클래스 이름][position]

        binding.[뷰 이름].[타입] = [클래스 변수 이름].[변수 이름] // 값 이름

        return binding.root
    }
}
```
리스트 뷰를 띄울 레이아웃에서는 다음과 같이 넣을 수 있다.  
```kotlin
val Adapter = [어뎁터 이름](this, [화면에 표시할 데이터])
binding.[뷰 이름].adapter = Adapter
```
추가로 각 리스트 뷰에 이벤트를 추가하는 방법은 아래와 같다.  
```kotlin
binding.[레이아웃 이름].onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
    val selectItem = parent.getItemAtPosition(position) as [클래스 이름]

    // selectItem.[변수 이름] 으로 데이터 출력 가능
}
```

## 내비게이션 뷰  
[Gradle Scripts] -> [build.gradle] 에 아래와 같은 코드를 추가  
```kotlin
dependencies {

    // 생략

    implementation 'com.google.android.material:material:1.9.0'
    
    // 생략
}
```
그 후 Sync Now 링크를 클릭하여 싱크  

[app] -> [res] 에서 우클릭 후 [New] -> [Android Resource Directory] 클릭 후 나오는 창에서 [Resource type] 에서 menu를 선택  
생성된 [app] -> [res] -> [menu] 폴더에서 우클릭 후 [New] -> [Menu Resource File] 클릭 후 나오는 창에서 [File name]에 snake_case 파일명 [네비게이션 메뉴] 지정  
[네비게이션 메뉴]에 group을 추가하고 checkableBehavior을 single로 설정  
추가하고 싶은 메뉴 만큼 Menu item을 추가하고 id, title, icon 등을 설정  
NavigationView를 화면에 배치하고 menu 속성을 [네비게이션 메뉴]로 지정, layout_gravity 속성을 start로 지정, id 지정  

인터페이스 상송
```kotlin
class [엑티비티이름] : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
```

onNavigationItemSelected 함수 오버라이딩  
```kotlin
override fun onNavigationItemSelected(item: MenuItem): Boolean {

    when(item.itemId) {
        R.id.[아이템의 아이디] -> [동작]
    }
    binding.[DrawerLayout 아이디].closeDrawers()    // 내비게이션 닫음
    return false
}
```

클릭되면 내비게이션이 나올 수 있도록 클릭 이벤트 연결  
```
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    binding.[이미지 뷰 이름].setOnClickListener {   // 이미지 뷰를 클릭하면
        binding.[DrawerLayout 아이디].openDrawer(GravityCompat.START)  // LEFT에서 시작해서 밀기
    }

    binding.[NavigationView 이름].setNavigationItemSelectedListener(this)
}
```

내비게이션 바가 열려 있을 경우 뒤로가기 버튼을 누르면 내비게이션만 닫힐 수 있도록  
```kotlin
override fun onBackPressed() {
    if(binding.[DrawerLayout 아이디].isDrawerOpen(GravityCompat.START)){   // 내비게이션바가 열려 있을 경우
        binding.[DrawerLayout 아이디].closeDrawers()
    }
    else{   // 닫혀 있을 경우
        super.onBackPressed()
    }
}
```

## Shared Preferences  
[Gradle Scripts] -> [build.gradle] 에 아래와 같은 코드를 추가  
```kotlin
dependencies {
    
    // 생략

    implementation 'androidx.preference:preference-ktx:1.2.1'
    
    // 생략
}
```
그 후 Sync Now 링크를 클릭하여 싱크  

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    binding.[버튼 아이디].setOnClickListener {
        saveData()  // 데이터 저장

        val intent = Intent(this, [이동할 엑티비티 이름]::class.java)
        startActivity(intent)
    }

    loadData()  // 데이터 불러오기
}

private fun loadData() {
    val pref = PreferenceManager.getDefaultSharedPreferences(this)  // import androidx.preference.PreferenceManager 인지 확인

    binding.[데이터를 쓸 곳].setText(pref.getString("[키]", "[키가 없을 경우의 값]"))
}    

private fun saveData() {
    val pref = PreferenceManager.getDefaultSharedPreferences(this)
    val edit = pref.edit()  // edit을 수정하여 값을 변경

    edit.putString("[키]", "[데이터]")
    edit.apply()    // 적용
}
```

## RecyclerView  
리스트 뷰의 상위호환 버전이기 때문에 리사이클러 뷰를 많이 사용할 것  
Adapter는 아래와 같은 기본 틀을 갖음  
```kotlin
class [어뎁터 이름](val [리스트 이름]: ArrayList<[리스트 클래스]>) : RecyclerView.Adapter<[어뎁터 이름].CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): [어뎁터 이름].CustomViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return [리스트 이름].size
    }

    override fun onBindViewHolder(holder: [어뎁터 이름].CustomViewHolder, position: Int) {
        val [변수 이름] = [리스트 이름][position]
        holder.bind([변수 이름])
    }

    class CustomViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind([변수 이름]: [변수 클래스]) {
            binding.[레이아웃 변수 아이디].text = [변수 이름].[클래스 속성]
        }
    }

}
```

리사이클러 뷰를 적용할 곳에는 다음과 같은 기본 틀을 갖음  
```kotlin
binding.[리사이클러 뷰 아이디].layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)   // VERTICAL은 세로로
binding.[리사이클러 뷰 아이디].setHasFixedSize(true) // 성능 개선

binding.[리사이클러 뷰 아이디].adapter = [어뎁터 이름]([데이터])
```

## Fragment
[app] -> [java] -> [com.[페키지이름].[프로젝트 이름]] -> [New] -> [Fragment] -> [Gallery] -> [Fragment (Blank)]를 선택  
[Fragment Name] 을 PascalCase로 작성하되, [PascalCase]Fragment의 규칙을 지켜서 생성함  

생성된 프래그먼트는 아래와 같은 기본틀을 갖음  
```kotlin
class [프래그먼트 이름] : Fragment() {

    private lateinit var binding: [프래그먼트 이름]Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.[프래그먼트 이름], container, false)

        binding = [프래그먼트 이름]Binding.bind(view)    

        return view
    }
}
```
프래그먼트를 변경할 경우는 아래와 같이 동작  
```kotlin
private fun [함수 이름]() {
    val ft = requireActivity().supportFragmentManager.beginTransaction()
    val [프래그먼트 변수 이름] = [변경할 프래그먼트 이름]()

    ft.replace([갈아 끼울 곳의 ID], [프래그먼트 변수 이름])
    ft.addToBackStack(null) // 백 스택에 추가하면 뒤로 가기 버튼으로 이전 프래그먼트로 이동 가능
    ft.commit()
}
```

프래그먼트간 데이터를 전송할 경우는 아래와 같은 코드를 추가  
```kotlin
private fun [함수 이름]() {
    val ft = requireActivity().supportFragmentManager.beginTransaction()
    val bundle = Bundle()        
    val [프래그먼트 변수 이름] = [변경할 프래그먼트 이름]()

    bundle.putString("[키]", "[값]")

    ft.replace([갈아 끼울 곳의 ID], [프래그먼트 변수 이름])
    ft.addToBackStack(null) // 백 스택에 추가하면 뒤로 가기 버튼으로 이전 프래그먼트로 이동 가능
    ft.commit()
}
```

데이터를 받는 곳은 아래와 같은 코드로 받을 수 있음
```kotlin
binding.[데이터를 받을 곳].text = arguments?.getString("[키]").toString()
```