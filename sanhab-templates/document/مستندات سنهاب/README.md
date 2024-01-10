# Nasim External Webservices

# 10 Line, 3 Errors (Back, Front)

# MS.RS.VU.TD

# کاربرد پروژه

    ماژول های این پروژه جهت اتصال به سرویس ها بیمه مرکزی (سنهاب) به کار برده می شوند. زیربنای پروژه Django Rest framework است.

# لایه های پروژه
    این پروژه از 8 لایه زیر تشکیل شده است:
    
        Model
        Serializer
        Repository
        Service
        View
        URL
        Test
        Document

# تعداد کلاس های لازم برای هر سرویس

    Model - 1 - Per Table
    Serializer - 2 - Per Endpoint
    Repository - 1 - Per Table / Endpoint
    Service - 1 - Per Endpoint
    View - 1 - Per Endpoint
    URL - 1 Path Object - Per Endpoint
    Test - 1 - Per Endpoint
    Document - 1 Line - Per Endpoint

    *همه لایه های به ازای یک Endpoint یک کلاس دارند به جز لایه های Model و Repository که به ازای هر جدول یک کلا س دارند.

    **تمام لایه های یک APP در این پروژه باید یک Module جداگانه داشته باشند.

# نوشتن ارور ها

    1.برای هر فیلد باید در صورت امکان 3 نوع خطا نوشته شود:

        1.خطای اجباری بودن فیلد
        2.خطای مرتبط با معتبر بودن مقدار فیلد
        3.خطاهای این فیلد در ارتباط با فیلدهای دیگر

    2.برای ارورهای مربوط به ورودی فقط از ValidationError استفاده شود
    
    3.برای سایر خطاها از کلاس پایه NasimException استفاده شود

    4.به دلیل اینکه از سرویس های این پروژه در فرآیند استفاده می شود و در صورتی که وضعیت Response 200 نباشد، متغیر خروجی JBPM REST Task پر نمی شود، در ارورهایی که raise می شوند وضعیت 202 است.

# استفاده محجدد از منطق ارورهای Validation

    برای این منظور، یک ارور در 2 مرحله پیاده سازی می شود:

    1.اولا در کلاس SerializerFieldValidator یک تابع استایک تعریف شود که ValidationError تولید کند
            @staticmethod
            def test_something(field_name, field_value):
            params={"field_name": field_name, "value": value}
            if True:
                raise ValidationError(
                    ().format(**params)
                )

    2.یک Argument به تعریف Field در کلاس سریلایزر اضافه شود

        validators=[lambda value: SerializerFieldValidator.test_something('my_field_xxx', value)]

# ساختار پروژه
    
    موارد زیر در لایه اول پروژه قرار دارند:

        Dockerfile
        .git
        Virtual environment folder
        manage.py
        any_django_user_app



# یکپارچه سازی خروجی سرویس ها

    موارد زیر باید رعایت شوند:

        1. در تابع __call__ از کلاس UniformResponseMiddleware کلیه اقدامات لازم برای ایجاد آبجکت Result انجام شود.

        2.خروجی تمام سرویس ها در تمام حالت ها باید مطابق با آبجکت زیر باشد:

            {
              "result": None,
              "bussinessErrors": {
                  "code": "some string",
                  "message": "some message"
            }


# لاگ پروژه
    مواردی که لاگ می شوند:

        1.Request
        2.Response
        3.Exception

# کتابخانه اتصال به SQL Server
    5 مرحله زیر باید انجام شوند:

        الف - کتابخانه pyodbc 3.0 (or newer) نصب شود

        ب - کتابخانه زیر نصب شود:

            pip install mssql-django

        پ - در settings.py مقدار کلید ENGINE در دیکشنری DATABASES به mssql تغییر داده شود.

            'ENGINE': 'mssql'

        ت - در صورت بر خوردن به ارور Microsoft ODBC باید msodbcssql را نصب کنیم:

            sudo apt-get install msodbcsql17

        ث - با فرمان python manage.py shell و ایمپورت کردن یک کلاس مدل، اتصال به دیتابیس قابل تست است.

# زیر ساخت فایل PDF

    در صورت لزوم با کمک weasyprint می توان یک کلاس helper برای این منظور طراحی کرد:

    from weasyprint import HTML, CSS

    class CreatePDFView(APIView):

        # 6 steps
        def get(self, request):

            # 1/5 - Get name and age
            name = 'ّFrom request body'
            age = 'From request body'

            # 2/5 - Load the html and css files
            html_template = open('my_template.html', 'r').read()
            html_content = html_template.format(title=title)

            css_template = open('my_style.css', 'r').read()
            css_content = css_template.format(background_color=background_color)

            # 3/5 - Create weasy print objects for HTML and CSS
            html = HTML(string=html_content)
            css = CSS(string=css_content)
            
            # 4/5 - Create in-memory BytesIO buffer
            pdf_buffer = BytesIO()  

            # 5/5 - Generate PDF in memory
            html.write_pdf(pdf_buffer, stylesheets=[css])

            # 6/5 - Return Response from buffer
            return Response(
                content_type='application/pdf',
                streaming_content=pdf_buffer
            )

# Helper App

    توابع و کلاس های مشترک بین app های مختلف باید در این لایه قرار داده شوند. خوب است که هر کلاس یک Base کلاس داشته باشد.

# نظم در ارتباط بین لایه ها

    لایه Repository:
        تنها Manager یک کلاس مدل را صدا می زند

    لایه Service:
        می تواند 1 کلاس Repository و چند کلاس Service دیگر را صدا بزند.

    لایه View:
        تنها یک کلاس سرویس را صدا می زند

# پابلیش روی سرور
    
    4 مرحله زیر لازم هستند:

        انجام دادن تست ها با runserver و در صورت نیاز docker-compose up

        ابتدا تغییر جدید را روی Version Control System پوش می کنیم.

        حذف فایل قدیمی و پابلیش فایل های جدید با ftp

        ریستارت کردن داکر


# سرو کردن فایل های استاتیک با Whitenoise

    الف - در settings.py STATIC_URL و STATIC_ROOT تعریف شوند:

        STATIC_URL = '/static/'
        STATIC_ROOT = os.path.join(BASE_DIR, 'static')

        # Each app should have it's own static directory
        # Convention for static directory structure is (application_name/static/application_name)
        STATICFILES_DIRS = [
        os.path.join(BASE_DIR, application_name/static')    
        ]

    ب - اجرای فرمان collectstatic (در داکرفایل قبل از بالا آوردن سرور قرار داده شود)

    پ - نصب کتابخانه whitenoise با pip (قرار دادن آن در requirements.txt)

    ت - افزودن میان افزار:
        "whitenoise.middleware.WhiteNoiseMiddleware"

# خروجی 202 به جای 400

    در موتور فرآیند در صورتی که وضعیت پاسخ 200 نباشد، متغیر خروجی ذخیره نمی شود. ما در این جا نیاز داریم که متغیر خروجی ذخیره شود تا قابل استفاده در فرآیند JBPM باشد. لذا خطاهای 400 با استاتوس 202 برگردانده می شوند. به این منظور 2 المان زیر باید تغییر کند:

        Class NasimException
        Class SanhabSimplifiedSoapSV (Instead of service layers of endpoints)

    * تبدیل آرایه ارورهای سنهاب به ارورهای سیستم باید به عنوان یک تابع به کلاس SanhabSimplifiedSoapSV افزوده و در تابع call_sanhab استفاده گردد.

# تغییر کلید detial به message در خروجی ارورها

    در 3 المان زیر باید تغییر ایجاد شود
        Function serializer_errors_to_bussinessErrrors
        Function convert_response_object_to_json
        Class SanhabSimplifiedSoapSV (Instead of service layers of endpoints)

# اختصاصی کردن فیلدهای Serializer و Default Error Messages

    در فایل customizers.py کلاس های مورد نیاز را تعریف می کنیم - استثنائا این فایل حاوی بیشتر از یک کلاس است تا به راحتی بتوان مانند ماژول rest_framework.serializers از آن استفاده کرد. 

    *تنها لازم است که دیکشنری error_messages را تغییر بدهیم و در صورت لزوم می توان به آرایه validator هم مقادیری را اضافه نمود

    class CustomIntegerField(serializers.IntegerField):

        def __init__(self, **kwargs):
            self.persian_name = kwargs.pop('persian_name', None)
            if self.persian_name == None:
                class_name = str(self.__class__)
                raise TypeError('آرگیومنت persian_name برای فیلد {class_name} customizer اجباری است'.format(class_name=class_name))
            # Just Change this dictionary in each calss
            self.error_messages = {
                'required': 'وارد کردن فیلد {persian_name} اجباری است'.format(persian_name = self.persian_name),
                'null': 'فیلد {persian_name} نمی تواند خالی باشد'.format(persian_name = self.persian_name),
                'invalid': 'مقدار فیلد {persian_name} باید یک عدد صحیح باشد'.format(persian_name = self.persian_name),
                #ٌWe format persian_name here but max_value is formatted when called
                'max_value': 'مقدار فیلد {persian_name} باید از '.format(persian_name = self.persian_name) + '{max_value} کمتر باشد',
                'min_value': 'مقدار فیلد {persian_name} باید از '.format(persian_name = self.persian_name) + '{min_value} بیشتر باشد',
                'max_string_length': 'رشته ارسالی به فیلد {persian_name} بیش از اندازه بزرگ است'.format(persian_name = self.persian_name)
            }
            # Validators: self.validators.append([lambda value: SerializerFieldValidator.test_something(self.persian_name, value)])
            super().__init__(error_messages=self.error_messages, **kwargs)

# Customizable Attributes

    Attribute: sanhab_lookup_needing_keys
    Place: SanhabClassSerializer extending classes
    Usage: افزودن فیلدهایی که از جدول لوکاپ نیاز به مپینگ دارند

    Attribute: internal_to_sanhab_dict
    Place: SanhabClassSerializer extending classes
    Usage: افزودن مپینگ فیلد سریلایزر به فیلد سنهاب

    Argument: persian_name
    Place: All customizers.py classes
    Usage: افزودن نام فیلد برای فرمت استرینگ ارور

    Argument: is_digits
    Place: CustomCharField
    Usage: برای فیلدهایی که باید فقط از ارقام تشکیل شده باشند معادل True ست می شود

    Argument:exact_length
    Place: CustomCharField
    Usage: برای فیلدهایی که باید دقیقا طول مشخصی داشته باشند (مثل شماره همراه که 11 رقم است) ست می شود


# ایجاد یک Endpoint جدید



    1.طراحی سریلایزر به همراه ارورهایش - می توان از فایل اکسل پیش ساخته استفاده کرد

        from helpers.custom_serializer_fields import customizers
        from helpers.sanhab_class_serializer import SanhabClassSerializer

    2.لایه View مطابق فرمت زیر است - بخش هایی که @@@ دارد باید جایگذاری شود

        class @@@(SanhabSimplifiedSoapSV):
        serializer_calss = @@@
        service_name = @@@
        method_name = @@@
        
        @swagger_auto_schema(query_serializer=@@@و  operation_summary=@@@)
        def @@@(self, request):
            return self.handle_reqeust(request)

# نوشتن تست
    
    برای هر Endpoint یک کلاس تست در یک فایل جدا و در پکیج tests ایجاد شده و موارد زیر در آن رعایت می شوند:

        1.از کلاس Parent مناسب با Endpoint استفاده شود:

            Unit test: SimpleTestCase (No DB - No Transaction)
            Integration test: TestCase (DB Related Logic - With Transaction)

        2.نام فایل از فرمت زیر پیروی می کند. همچنین نام کلاس و نام متد هم با test آغاز می شوند.

            (test + _view_name)

        3. متد تست نوشته شده و از self.assert در آن استفاده می شود.

        *مثال از کلاس تست:

            class TestSomeClass(TestCase):
    
                def test_something(self):
                    self.assertEqual(1, 1)

        4.برای اجرای تست لایه view از RequestFactory استفاده می شود - با این کلاس می توان یک تابع View را همانند یک تابع معمولی و با پر کردن request صدا زد.

# اجرای تست

    python manage.py test app_name

# ایجاد کلاس Serializer از روی مستند سنهاب

    یک فایل اکسل با ستون های زیر می تواند دو نوع خطا (اجباری بودن و مقدار) و همجنین مپینگ (فیلد داخلی به سنهاب و همچنین لوکاپ مپینگ) را پاسخگو باشد، تنها نوشتن ارورهای پیچیده (الگوریتم ها یا در ارتباط با فیلدهای دیگر) باقی می ماند.

        SanhabFieldName - ستون مستند
        SanhabFieldType - ستون مستند
        Nullable - ستون مستند
        SanhabDescription - ستون مسنند
        SanhabCodingColumn - ستون مستند
        is digits
        exact_length
        FieldParentClass - طراحی بک اند
        DecriptiveFieldName - نام گذاری بک اند
        P1 - =CONCATENATE(@@@DescriptiveFieldName, " = ")
        P2 - =VLOOKUP(@@@SanhabFieldType, customizers!$A$1:$B$50, 2, 0)
        P3 - , required=
        P4 - =IF(@@@SanhabNullable="No", "True, ", "False, ")
        P5 - =CONCATENATE("description='", @@@SanhabDescrption, "'")
        P6 - =IF(@@@SanhabCodingColumn="", ")", ", has_lookup_mapping = True)")
        Field - =CONCATENATE(H29, I29, J29, K29, L29, M29)
        internal_to_sanhab_dict - =CONCATENATE("'", @@@DescriptiveName, "': '", @@@SanhabName, "',")

    *چنین فایل اکسلی تحت عنوان serializer_creator در روت پروژه موجود است.

    ** ستون های نتیجه:
        1.ستون فیلد سریلایزر
        2.ستون دیکشنری داخلی به سنهاب

# حداقل تست لازم برای هر سرویس

    یک کلاس تست که دارای یک متد با شرایط زیر باشد:

        1.تمام پارامترها پر شوند - اطمینان از عدم ارسال پارامتر شناخته نشده

        2.حداقل یک جمله ارور ثابت که داده می شود assert شود - تست ارتباط موفق با سنهاب

        *نام کلاس بر اساس نام View و با پیشوند Test خواهد بود

        **نام متد:
            test_full_params_and_one_error


    