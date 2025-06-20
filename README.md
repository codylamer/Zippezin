# 📦 Zippezin — Denizen Plugin for .zip Archive Management

Are you a **Denizen** scripter who needs to extract or compress `.zip` files right from your scripts?

Introducing **Zippezin** — a lightweight, Denizen-compatible plugin that lets you work with `.zip` archives through simple script commands.

---

## ✨ Features

- 📂 Extract ZIP files to specific folders
- 🗜️ Compress folders (or their contents) into ZIP archives
- 🧠 Smart path handling (e.g., `plugins/MyFolder/` compresses folder contents only)
- 🧵 Waitable processing support with `~waitable` commands
- ✅ Success status returned for easy scripting logic

---

## 🧰 Commands

### 🔓 `~extract`

Extracts a zip archive into a folder.

**Syntax:**

```denizen
- ~extract origin:<path_to_zip> destination:<output_folder> (overwrite) save:<entry>
```

**Example:**

```denizen
- ~extract origin:data/archive.zip destination:data/output/ overwrite save:unzip
- narrate "Extraction success: <entry[unzip].success>"
```

If the `destination` exists and `overwrite` is not specified, the command fails.

---

### 🧳 `~compress`

Compresses a folder or its contents into a `.zip` archive.

**Syntax:**

```denizen
- ~compress origin:<folder> destination:<zip_file> (overwrite) save:<entry>
```

**Behavior:**
- If `origin:` ends with `/`, only the folder’s contents are zipped.
- Otherwise, the folder itself (as a root entry) is zipped.

**Example:**

```denizen
- ~compress origin:plugins/MyPlugin/ destination:data/myplugin.zip overwrite save:zip
- narrate "Compression success: <entry[zip].success>"
```

---

## 💬 Requirements

- [Denizen](https://github.com/DenizenScript/Denizen)

Drop `Zippezin.jar` into your `plugins` folder and reload the server. That’s it!

**Unleash the power of archives inside your Denizen scripts!**

---

## 🔒 Zippezin — OpenSource Software

**Author / Owner:** CodYLameR

**Original Creator:** CodYLameR

**License:** Apache 2.0

---

---

# 📦 Zippezin — Плагин для работы с архивами `.zip` в Denizen

Ты Denizen-скриптер и хочешь распаковывать или сжимать `.zip` архивы прямо из скриптов?

Познакомься с **Zippezin** — лёгким плагином, который добавляет команды для работы с архивами в твою Denizen-среду.

---

## ✨ Возможности

- 📂 Распаковка `.zip` архивов в указанную папку
- 🗜️ Сжатие папок или их содержимого в `.zip`
- 🧠 Умная обработка путей (`plugins/MyFolder/` сжимает только содержимое)
- 🧵 Поддержка ожидаемых операций через `~waitable`
- ✅ Возвращаемое значение `success` для логики скриптов

---

## 🧰 Команды

### 🔓 `~extract`

Распаковывает `.zip` архив в указанную директорию.

**Синтаксис:**

```denizen
- ~extract origin:<путь_к_zip> destination:<папка_назначения> (overwrite) save:<entry>
```

**Пример:**

```denizen
- ~extract origin:data/archive.zip destination:data/output/ overwrite save:unzip
- narrate "Успешная распаковка: <entry[unzip].success>"
```

Если папка `destination` уже существует и `overwrite` не указан — команда завершится ошибкой.

---

### 🧳 `~compress`

Создаёт `.zip` архив из папки или её содержимого.

**Синтаксис:**

```denizen
- ~compress origin:<папка> destination:<архив.zip> (overwrite) save:<entry>
```

**Поведение:**
- Если `origin:` заканчивается `/`, архивируется **только содержимое** папки.
- Без `/` архивируется **сама папка** как корневая.

**Пример:**

```denizen
- ~compress origin:plugins/MyPlugin/ destination:data/myplugin.zip overwrite save:zip
- narrate "Успешное сжатие: <entry[zip].success>"
```

---

## 💬 Требования

- [Denizen](https://github.com/DenizenScript/Denizen)

Просто помести `Zippezin.jar` в папку `plugins` и перезагрузи сервер.

**Управляй архивами прямо из скриптов. Просто. Мощно. Асинхронно.**

Вот перевод этого блока на русский:

---

## 🔒 Zippezin — Программное обеспечение с открытым исходным кодом

**Автор / Владелец:** CodYLameR

**Первоначальный разработчик:** CodYLameR

**Лицензия:** Apache 2.0

---

