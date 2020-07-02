# Модуль Android разработка 2020
## Задание №1. Создать проект на GitHub
В проекте на GitHub добавить преподавателя как collaborator
Создать ветку от master
В этой ветке сгенерировать проект с пустой активностью
Сделать commit/push
Оформить Pull Request из ветки в master. В качестве аппрувера установить преподавателя

## Задание №2. Single Activity приложение списка контактов

Необходимо реализовать Single Activity приложение списка контактов(Пока только верстка и навигация)

### Составные компоненты(Смотрите рисунок во вложении):
#### MainActivity - главная и единственная активность в приложении
#### ContactListFragment - фрагмент со списком контактов
 - Короткая карточка контакта
 - Фотография контакта
 - Имя контакта
 - Телефон контакта

При клике пользователя на карточку контакта происходит переход на фрагмент деталей контактов. Необходимо в аргументах передавать ID выбранного контакта
#### ContactDetailsFragment
 - Фотография контакта
 - Имя контакта
 - Телефон контакта
 - 2-й телефон
 - e-mail контакта
 - 2-й e-mail контакта
 - Описание контакта

На экране списка контактов пока достаточно реализовать одну карточку контакта
### Результат
| Главный экран | Детали контакта |
| ------ | ------ |
| ![Список](https://i.imgur.com/uVBhVfT.jpg) |![Главный экран](https://i.imgur.com/rhFKsrB.jpg)

## Задание №3. Сервисы
Необходимо написать привязанный сервис, который будет иметь публичное API предоставляющее список контактов. Список контактов пока захардкодить в константу
Сервис должен предоставлять список контактов на другом потоке программы - т.е. должен создавать поток для загрузки списка контактов
### Сценарий использования:
- Главная активность привязывается к сервису с помощью bindService
- Получает публичный интерфейс сервиса
- Предоставляет публичный интерфейс сервиса фрагментам
- Фрагменты используют публичный интерфейс сервиса для получения данных контактов
- Публичный интерфейс должен иметь 2 метода: 
  1. Метод получения списка контактов с короткой информацией(Будет использоваться в фрагменте списка контактов) - метод должен быть асинхронным
  2. Метод получения детальной информации по ID контакта(Будет использоваться в фрагменте деталей контакта) - метод должен быть асинхронным
  
## Задание №4. BroadcastReceiver - Добавить напоминания о дне рождения

Необходимо добавить к деталям контакта поле "День рождения" в формате "число месяц"
А так же кнопку которая будет иметь 2 состояния - "Включить напоминания" и "Выключить напоминания"
Состояние кнопки должно зависеть от того, добавлена ли уже задача в AlarmManager или нет.
При нажатии кнопки в состоянии "Включить напоминания" - должна ставиться задача на вызов вашего BroadcastReceiver'а на число в поле "День рождения" в качестве данных в Intent должен передаваться ID выбранного контакта и текст напоминания "Сегодня день рождения у $s", где $s - имя контакта

## Задание №5. Content - Использование поставщика контактов

### Задание

Необходимо реализовать загрузку контактов пользователя из поставщика контактов В существующем методе загрузки списка контактов - получать список всех контактов пользователя из поставщика контактов В существующем методе загрузки деталей контакта по ID - получать все необходимые данные контакта из поставщика контактов по ID

### Проделанная работа.

Добавлен класс-репозиторий контактов ContactRepositoryFromSystem, который позволяет получать список всех контактов, список телефонов и адресов эл.почты и подробную информацию о контакте.

При старте программы добавлена проверка и запрос на предоставление прав для чтения контактов из системы.  

Ввиду того, что приложение постоянно крашилось и ругалось на то, что я пытаюсь управлять графическим интерфейсом не из того потока, пришлось проделать рефакторинг всего проекта.

Для повышения производительность модель описывающая контакт (`PersonModel`) разделена на две: 

- `PersonModelCompact` - модель используемая только в списке контактов
- `PersonModelAdvanced` - модель используемая во фрагменте деталей контакта. 

Хранение фотографии контактах в моделях заменено с `Resource ID` на `URI`. 

Пришлось отказаться от разделения на Фамилию Имя и Отчество. Вместо этого использовать объединенное поле `String displayName;`

Помимо этого переделал фейковый репозиторий контактов `ContactRepositoryFakeImp`  для работы с новой моделью контакта.

## Задание №6. Рефакторинг проекта по паттерну MVP

### Задание

Необходимо произвести рефакторинг существующего кода приложения с использованием на выбор: 1. MVVM - использовать компоненты от Google: ViewModel + LiveData 2. MVP - в качестве библиотеки использовать Moxy В рамках рефакторинга необходимо будет удалить существующий сервис загрузки контактов, и всю работу по загрузке контактов из сервиса вынести в репозиторий, который будет использоваться в ViewModel/Presenter соответственно выбранного паттерна проектирования.

### Проделанная работа.

Произвел рефакторинг и изменение архитектуры на MVP с использованием библиотеки Moxy. 

Удалил существующий сервис загрузки контактов

Всю работу по загрузке контактов перенс в репозиторий



## Задание 7. Рефакторинг списка контактов на RecyclerView

### Задание 

Добавить поиск по именам. Лучше использовать SearchView. Каждый новый ввод символа в SearchView - должен приводить к пере-запросу данных списка контактов из репозитория. При пустом вводе - выводится весь список контактов Отслеживание изменений через DiffUtils. Желательно производить diff на отдельном потоке, для этого можно воспользоваться готовой реализацией адаптера - ListAdapter Написать свою реализацию разделителей между элементами, через ItemDecorator

### Проделанная работа.

- Заменил ListView на RecyclerView;

- Адаптером для RecyclerView служит ListAdapter;

- Добавил поиск по именам (SearchView);

- Для отслеживания изменени использую DiffUtils;

Каждый новый ввод символа в SearchView - должен приводить к пере-запросу данных списка контактов из репозитория. При пустом вводе - выводится весь список контактов

## Задание 8. Рефакторинг с RxJava

Рефакторинг существующего приложения с использованием RxJava

1. Получение данных в io потоке. Оператор subscribeOn 
2. Отображение данных на главном потоке. Оператор observeOn 
3. Индикация загрузки данных. Смотрите в сторону операторов doOnSubscribe и doFinally/doOnTerminate 
4. Учитывайте lifecycle компонентов. Disposable.dispose() - в правильных местах.

### Проделанная работа.

Провел рефакторинг существующего приложения с использованием RxJava. 

1. Добавил библиотеку RxJava и RxAndroid
2. Методы репозитория возвращают `Observable`
3. Получение данных в io потоке.
4. Индикация загрузки данных c помощью прогресс бара;
5. Учтен lifecycle компонентов;

## Задание 9. Использование Dagger

### Задание

Произвести рефакторинг приложения  с использованием DI. В качестве DI фрэймворка использовать Dagger 2. В задании следует использовать различные Scopes: 

- @Singleton для зависимостей предоставляемых на время жизни всего приложения
- @ContactsListScope - для зависимостей предоставляемых на время жизни Presenter/ViewModel экрана списка контактов 
- @ContactsDetailsScope - для зависимостей предоставляемых на время жизни Presenter/ViewModel экрана деталей контактов. 

**Зависимости между компонентами на выбор:** 

- Через component dependency 
- Через  subcomponents

### Проделанная работа 

- Добавлен фреймворк Dagger2
- Произведен рефакторинг кода с применением DI:
  - модуль для репозитория контактов
  - subcomponent для списка контактов
  - subcomponent для деталей контакта
- Зависимости между компонентами реализованы через `subcomponents `
- Используются разные скоупы для компонентов:
  - `@Singleton`
  - `@ContactListScope`
  - `@ContactDetailsScope`