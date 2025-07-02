# DiamondReports - Система модерации для Minecraft

Плагин для обработки жалоб игроков с интеграцией Discord и LiteBans. Предоставляет удобный интерфейс для модераторов и автоматическую очистку репортов при бане игроков.

## Основные возможности

- 📝 Создание жалоб на игроков через команду `/report`
- 📊 Удобное и красивое интерактивное меню для просмотра и обработки репортов через команду `/reports` с подробнейшем описанием, поставил защиту от "дурака" везде где только мог, меню максимально удобное и пользовательски-безопасное, убедитесь сами ;D
- 👮‍♂️ Автоматическое уведомление модераторов о новых жалобах
- 🔄 Синхронизация с LiteBans (автоудаление репортов при бане)
- 📨 Отправка уведомлений в Discord о принятых жалобах
- 🔍 Сортировка репортов по дате и статусу обработки

## Технические особенности

- **Динамические инвентари**  
  Кастомные GUI с пагинацией и сортировкой элементов  
  [Spigot Inventory API](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/package-summary.html)
- **Асинхронная интеграция с Discord**  
  Отправка вебхуков без блокировки основного потока  
  [Discord Webhook Documentation](https://discord.com/developers/docs/resources/webhook)
- **Событийно-ориентированная архитектура**  
  Обработка действий игроков через слушатели событий  
  [Spigot Event API](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/package-summary.html)
- **Конфигурируемые сообщения**  
  Гибкая настройка текстов через config.yml

## Ключевые технологии

- **[Spigot API 1.17+](https://www.spigotmc.org/wiki/spigot/)** - Базовое API для разработки плагинов
- **[LiteBans API](https://github.com/ruany/LiteBans)** - Интеграция с системой банов
- **[Java SimpleDateFormat](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html)** - Форматирование дат и времени
- **[SkullMeta](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/meta/SkullMeta.html)** - При открытии меню репортов будут поочерёдно видны головы игроков (с их уникальными скинами) для упрощённой работы модерации.

## Структура проекта

```
DiamondReports/
├── src/
│   └── main/
│       ├── java/
│       │   └── me/
│       │       └── gusandr/
│       │           └── diamondreports/
│       │               ├── Plugin.java            # Основной класс плагина
│       │               ├── util/
│       │               │   └── Utils.java         # Вспомогательные методы
│       │               ├── menu/
│       │               │   ├── ReportMenu.java     # GUI для репортов
│       │               │   ├── item/
│       │               │   │   └── ItemReport.java # Элемент репорта
│       │               │   ├── event/
│       │               │   │   └── MenuEvent.java  # Обработка действий в GUI
│       │               │   └── command/
│       │               │       └── ReportMenuCMD.java # Команда меню
│       │               └── report/
│       │                   └── command/
│       │                       ├── ReportCMD.java      # Команда репорта
│       │                       └── ReportCompleter.java # Автодополнение
│       └── resources/
│           ├── config.yml       # Конфигурация плагина
│           └── plugin.yml       # Метаданные плагина
└── pom.xml                      # Конфигурация Maven
```

### Описание директорий
- `menu/`: Содержит логику GUI для работы с репортами
- `report/command/`: Обработчики команд для создания и управления репортами
- `util/`: Вспомогательные утилиты и методы
- `resources/`: Файлы конфигурации и метаданных

## Конфигурация

```yaml
item-report:
  name: "&2&l%suspectPlayer"
  lore:
    - "&6Отправил %whoSendReport"
    - "&dДата: &n%date&f"
    - "&cОписание: &l%moreInfo"
    - "Кликни что бы начать взаимодействие с репортом!"
    - "&cРепорт занят? &l%complaintTaken"

date:
  format: "dd.MM.yyyy HH:mm:ss"

discord:
  webhooks: "https://discord.com/api/webhooks/xxx/xxxxx"

text:
  message:
    new-report-send: "&cМодераторы! Новый репорт! Смотрите /reports"
    it-worked: "&cУспех!"
```

## Интеграция с LiteBans

Плагин автоматически удаляет репорты при бане игрока через плагин LiteBans:

```java
litebans.api.Events.get().register(new litebans.api.Events.Listener() {
    @Override
    public void entryAdded(litebans.api.Entry entry) {
        if (entry.getType().equals("ban")) {
            UUID playerSuspectUUID = UUID.fromString(entry.getUuid());
            ReportMenu.deleteReport(playerSuspectUUID);
        }
    }
});
```
