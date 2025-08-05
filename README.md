# 📬 Gmail Email Fetcher – Task 2

This Spring Boot project connects to the Gmail API and fetches the **latest 200 emails**, returning their **sender** and **subject**.

---

## ✅ Features

- Connects to Gmail using OAuth2.
- Fetches the latest 200 emails.
- Returns JSON with sender and subject.
- Tested and working with real Gmail account (see sample output).

---

## ⚙️ How to Run the Application

### Requirements:

- Java 17+
- Maven installed (only for building, not needed for `.jar`)

### Steps:

```bash
cd src\main\java\com\example\demo_task2
run DemoTask2Application.java file

http://localhost:8081/emails
```

Note
Due to Gmail API restrictions:

OAuth2 access is account-specific.

You won’t be able to fetch emails from my Gmail unless your Gmail account is authorized.

To demonstrate functionality, see sample-output.json (attached).

You can add credentials.json using your account from 
  Go to Google Cloud Console
  Create a new project.
  
  Enable the Gmail API under APIs & Services > Library.
  
  2. Create OAuth 2.0 Credentials
  Go to APIs & Services > Credentials
  
  Create OAuth Client ID:
  
  Application type: Desktop App
  
  Download the credentials.json file.
