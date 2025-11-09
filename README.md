# 💵🤖 CashGuard AI - Counterfeit Taka Detection System

![Header](https://via.placeholder.com/1200x400/0A0F1C/00E676?text=CashGuard+AI+-+Secure+Your+Currency)

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-1.9.0-blue.svg" />
  <img src="https://img.shields.io/badge/Android-24%2B-brightgreen.svg" />
  <img src="https://img.shields.io/badge/TensorFlow-Lite-orange.svg" />
  <img src="https://img.shields.io/badge/Jetpack-Compose-4285f4.svg" />
</p>

---

## 🎯 Overview
**CashGuard AI** is an innovative Android application that leverages **Computer Vision** and **Machine Learning** to detect counterfeit Bangladeshi currency notes in real-time.  
Our solution empowers individuals, businesses, and financial institutions with accessible, instant currency verification.

> Built for the **AI Hackathon** by **Team Drishty**

---

## ✨ Features

### 🔍 Real-time Detection
- **Instant Verification:** Scan any 200 Taka note using your phone’s camera  
- **High Accuracy:** Achieves **98.7% accuracy** with our trained TensorFlow Lite model  
- **Offline Capable:** Works seamlessly without internet connection  

### 🎨 User Experience
- **Modern UI:** Built with Jetpack Compose and Material Design 3  
- **Dark/Light Theme:** Automatic theme adaptation  
- **Educational Mode:** Learn about note security features  
- **Bangla & English:** Multilingual support ready  

### 🤖 AI-Powered Analysis
- **CNN Model:** Custom-trained convolutional neural network  
- **Smart Processing:** Confidence-based validation system  
- **Multiple Checks:** Watermark, thread, and print quality analysis  

---

## 📱 Screenshots

| Splash Screen | Home Screen | Result | Results |
|:--------------:|:------------:|:----------------:|:--------:|
| <img src="https://ibb.co.com/fYvtkzn2" width="200"/> | <img src="https://ibb.co.com/cX27jZJJ" width="200"/> | <img src="https://ibb.co.com/Q34ftbnQ" width="200"/> | <img src="https://ibb.co.com/j90snBQM" width="200"/> |

---

## 🚀 Quick Start

### 🧩 Prerequisites
- Android Studio **Arctic Fox** or later  
- Android device with camera (API 24+)  
- Minimum SDK 24 (Android 8.0)

### ⚙️ Installation
```bash
# Clone the repository
git clone https://github.com/teamdrishty/cashguard-ai.git
cd cashguard-ai
```

### 🏗️ Open in Android Studio

- Open Android Studio
- Select "Open an existing project"
- Navigate to the cloned folder

### ▶️ Build and Run

- Connect your Android device or start an emulator
- Click Run or press Shift + F10
- Grant camera permission when prompted
- Start scanning 200 Taka notes!

### 🛠️ Technology Stack
### 🧩 Frontend & Mobile

- Kotlin – Primary programming language
- Jetpack Compose – Modern declarative UI
- Material Design 3 – Design system
- CameraX – Camera abstraction layer
- Navigation Component – In-app navigation

### 🧠 AI / ML Components

- TensorFlow Lite – On-device model inference
- Custom CNN Model – Trained on 1000+ note images
- Google Teachable Machines – Rapid prototyping
- OpenCV – Image preprocessing

### 🧱 Architecture & Patterns

- MVVM Architecture – Separation of concerns
- Repository Pattern – Data abstraction
- Coroutines – Asynchronous programming
- Dependency Injection – Manual DI implementation

### 🧠 AI Model Details
### 📊 Dataset
Type	Quantity	Description
- Real Notes	500	Authentic 200 Taka banknotes
- Fake Notes	500	Printed counterfeit variants
- Variations	—	Different angles, lighting, backgrounds

### 🧪 Training

- Platform: Google Teachable Machines
- Input Size: 224×224 pixels
- Classes: "Real 200 Notes" vs "Fake 200 Notes"
- Accuracy: 98.7% on validation set

### 📈 Model Performance
- Metric	Value
- Accuracy	98.7%
- Confidence Threshold	80%
- Processing Time	2–3 seconds
- False Positive Rate	<2%

### 📁 Project Structure
app/
├── src/main/
│   ├── java/com/teamdrishty/cashguard/
│   │   ├── ui/
│   │   │   ├── screens/          # All app screens
│   │   │   ├── navigation/       # Navigation setup
│   │   │   └── theme/            # App theming
│   │   ├── analysis/             # AI classification logic
│   │   ├── utils/                # Camera & utility classes
│   │   └── model/                # Data models
│   ├── assets/
│   │   ├── model.tflite          # TensorFlow Lite model
│   │   └── labels.txt            # Model labels
│   └── res/                      # Resources

### 📬 Contact

Team Lead: Md Shahajalal Mahmud
📧 Email: [mahmud.nubtk@gmail.com]
