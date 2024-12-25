## Миксины

С помощью миксинов можно хранить некоторую функциональность отдельно от классов, 
в которых она используется. При этом они stateless - т.е. содержат только функции, и не содержат внутренних переменных - по замыслу, это кусочек поведения, который можно добавлять в будущие объекты по мере необходимости. 

Так можно гибко комбинировать различные наборы функций,
не привязываясь к телу конкретного класса - т.е. впоследствии не нужно будет и лезть внутрь,
чтобы радикально что-то изменить. 

Проще редактировать -> проще поддерживать -> проще расширять.

### Реализация
И в Java, и в Kotlin миксины реализуются не напрямую (как в JS), и нужного поведения можно добиваться либо при помощи интерфейсов с методами по умолчанию, либо при помощи паттерна Visitor. В Kotlin, кроме этого, есть extension-функции.

Пример использования:
````Kotlin

// первый кусок функциональности - Context "не знает", что у него появилась новая функция
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

// второй - аналогичным образом определяем частый запрос на предоставление разрешения от ОС
fun AppCompatActivity.requestPermission(permission: String, requestCode: Int, onGranted: () -> Unit, onDenied: () -> Unit) {
    if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
        onGranted()
    } else {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    }
}

//использование обоих расширений в одном классе
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        showToast("а я заработало!")


        requestPermission(
            permission = Manifest.permission.CAMERA,
            requestCode = 100,
            onGranted = { showToast("Можно снимать") },
            onDenied = { showToast("Отказ") }
        )
    }
}

// .. и методы теперь можно использовать во всех Context и Activity проекта
````