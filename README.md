# Wires

![image](https://res.cloudinary.com/hnp4q7akq/image/upload/v1655150453/Introduction_1_miqfw0.png)

![Kotlin](https://img.shields.io/badge/kotlin-%230095D5.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84.svg?style=for-the-badge&logo=android-studio&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-039BE5?style=for-the-badge&logo=Firebase&logoColor=white)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)

Wires - социальная сеть для представителей IT-профессий.

# Функционал
- управление аккаунтом - регистрация, вход, выход
- создание/редактирование/удаление постов
- установка лайков и добавление комментариев к постам
- создание личных и групповых каналов общения
- обмен текстовыми сообщениями внутри каналов (протокол WebSocket)
- редактирование личных данных
- изменение пароля
- изменение локализации (Русский, English)
- возможность отправки баг-репортов

# Стек
- 100% [Kotlin](https://kotlinlang.org/)
- Android Jetpack (ViewModel, LiveData, NavigationComponent)
- MVVM (Архитектурный паттерн)
- [Retrofit](https://github.com/square/retrofit) (Сеть)
- [Glide](https://github.com/bumptech/glide) (Загрузка изображений)
- [Dagger 2](https://github.com/google/dagger) (Внедрение зависимостей)
- [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines) (Асинхронность)
- [PaperDB](https://github.com/PaperMC/Paper) (Локальное хранение данных)
- [NV-Websocket-Client](https://github.com/TakahikoKawasaki/nv-websocket-client) (Клиент Websocket)
- [Firebase Cloud Messaging](https://firebase.google.com) (Пуш-уведомления)

# Архитектура
Архитектурный паттерн - MVVM.

Слои:
- Слой интерфейса (Presentation) – слой, представленный активностями и фрагментами, в котором располагается логика отрисовки интерфейса. Также здесь хранятся все вспомогательные классы, связанные с интерфейсом, например, пользовательские представления.
- Слой логики экранов (ViewModel) – слой, представленный потомками класса ViewModel, в котором располагается логика взаимодействия экрана с другими слоями. Все события, происходящие на экране и все данные, получаемые экраном хранятся в данном слое.
- Слой бизнес-логики (UseCase) - слой, представленный юзкейсами, которые представляют собой сценарии обращения к источникам данных со всей сопутствующей логикой. Могут быть переиспользованы с различных экранов.
- Доменный слой (Repository) – слой, представленный репозиториями, специализированными классами, которые оперируют источниками данных и решают, к какому из них обратиться. Здесь содержится логика получения данных по запросу экранов и логика преобразования данных из одного типа в другой (mappers).
- Слой источников данных (DataSource) – слой, представленный классами, отвечающими за конкретные источники данных, такие как сервер или локальная база данных. Они работают напрямую с источниками данных, отправляют и получают из них информацию, передаваемую в репозитории.

# Бэкенд

Приложение работает на основе собственного бэкенда - [wires-backend](https://github.com/MegaR1CK/wires-backend).

# Установка

Приложение работает на устройствах с OC Android версии 6.0 (API 23) и выше.

Установочный файл можно скачать по ссылке - [wires-android.apk](https://disk.yandex.ru/d/4yGLkjqEUBWiVA).
