# Testing Workflow - Visual Guide

## Complete Testing Journey

```
START
  ↓
┌─────────────────────────────────────────┐
│ Step 1: Navigate to Project             │
│ cd D:\...\java-uml-generator            │
└─────────────────────────────────────────┘
  ↓
┌─────────────────────────────────────────┐
│ Step 2: Build Project (First Time Only) │
│ mvn clean compile                       │
│ ⏱️  Builds in 3-5 seconds               │
└─────────────────────────────────────────┘
  ↓
┌─────────────────────────────────────────┐
│ Step 3: Prepare Java File               │
│ Option A: Use sample from src/samples/  │
│ Option B: Create your own               │
└─────────────────────────────────────────┘
  ↓
┌─────────────────────────────────────────┐
│ Step 4: Choose Output Format            │
├─────────────────────────────────────────┤
│ A) TEXT      (Human readable)           │
│ B) MERMAID   (GitHub compatible)        │
│ C) PLANTUML  (Professional)             │
└─────────────────────────────────────────┘
  ↓
┌─────────────────────────────────────────┐
│ Step 5: Run Tool                        │
│                                         │
│ TEXT FORMAT:                            │
│ .\run-uml.ps1 <file>                    │
│                                         │
│ MERMAID FORMAT:                         │
│ .\run-uml-diagram.ps1 <file> mermaid    │
│                                         │
│ PLANTUML FORMAT:                        │
│ .\run-uml-diagram.ps1 <file> plantuml   │
└─────────────────────────────────────────┘
  ↓
┌─────────────────────────────────────────┐
│ Step 6: Get Results                     │
│ - View in console                       │
│ - Save to file                          │
│ - Paste into documentation              │
│ - Use in GitHub README                  │
└─────────────────────────────────────────┘
  ↓
SUCCESS ✅
```

---

## Quick Decision Tree

```
What do you want to do?
│
├─ "I want to quickly see the structure"
│  └─ USE TEXT FORMAT
│     .\run-uml.ps1 src/samples/Order.java
│
├─ "I want to use it in GitHub README"
│  └─ USE MERMAID FORMAT
│     .\run-uml-diagram.ps1 src/samples/CreditCardPayment.java mermaid
│
├─ "I want professional diagrams"
│  └─ USE PLANTUML FORMAT
│     .\run-uml-diagram.ps1 src/samples/PaymentService.java plantuml
│
└─ "I want to save to a file"
   └─ PIPE TO FILE
      .\run-uml-diagram.ps1 <file> mermaid | Out-File diagram.mmd
```

---

## Information Flow

```
┌──────────────────┐
│  Your Java File  │
│  (.java)         │
└────────┬─────────┘
         │
         ↓
┌──────────────────────────┐
│  JavaSourceParser        │
│  - Reads file            │
│  - Uses JavaParser lib   │
│  - Creates AST           │
└────────┬─────────────────┘
         │
         ↓
┌──────────────────────────┐
│  ClassAnalyzer           │
│  - Walks AST             │
│  - Extracts classes      │
│  - Extracts fields       │
│  - Extracts methods      │
│  - Detects relationships │
└────────┬─────────────────┘
         │
         ↓
┌──────────────────────────┐
│  UML Model Objects       │
│  - UmlClass              │
│  - UmlField              │
│  - UmlMethod             │
│  - UmlRelationship       │
└────────┬─────────────────┘
         │
    ┌────┴────┬──────────┐
    │          │          │
    ↓          ↓          ↓
  TEXT      MERMAID   PLANTUML
  OUTPUT    OUTPUT     OUTPUT
    │          │          │
    └────┬─────┴──────┬───┘
         │            │
         ↓            ↓
     CONSOLE      GitHub/Docs
     OUTPUT       DIAGRAMS
```

---

## Format Comparison

```
┌──────────────────────────────────────────────────────────┐
│ TEXT FORMAT                                              │
├──────────────────────────────────────────────────────────┤
│ Class: Order                                             │
│ Package: com.example                                     │
│ Type: CLASS                                              │
│                                                          │
│ Fields:                                                  │
│   - gateway : PaymentGateway                             │
│                                                          │
│ Methods:                                                 │
│   + checkout() : void                                    │
└──────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────┐
│ MERMAID FORMAT (Copy-Paste into GitHub)                  │
├──────────────────────────────────────────────────────────┤
│ classDiagram                                             │
│     class Order {                                        │
│         - gateway: PaymentGateway                        │
│         + checkout() void                                │
│     }                                                    │
└──────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────┐
│ PLANTUML FORMAT (Professional Diagrams)                  │
├──────────────────────────────────────────────────────────┤
│ @startuml                                                │
│ class Order {                                            │
│     - gateway: PaymentGateway                            │
│     + checkout(): void                                   │
│ }                                                        │
│ @enduml                                                  │
└──────────────────────────────────────────────────────────┘
```

---

## Sample Test Sessions

### Session 1: Simple Class Testing
```
COMMAND:
.\run-uml.ps1 src/samples/Order.java

TIME: ~1 second

OUTPUT:
✅ Class name
✅ Package
✅ 3 fields
✅ 3 methods
```

### Session 2: Inheritance Testing
```
COMMAND:
.\run-uml-diagram.ps1 src/samples/CreditCardPayment.java mermaid

TIME: ~1 second

OUTPUT:
✅ Class name
✅ 2 fields
✅ 2 methods
✅ Inheritance relationship detected:
   CreditCardPayment --|> PaymentProcessor
```

### Session 3: Interface Testing
```
COMMAND:
.\run-uml-diagram.ps1 src/samples/PaymentService.java mermaid

TIME: ~1 second

OUTPUT:
✅ Class name
✅ 2 fields
✅ 4 methods
✅ Interface implementation detected:
   PaymentService ..|> IPaymentGateway
```

---

## Step-by-Step Visual Guide

### STEP 1️⃣: Open PowerShell
```
Windows + R
Type: powershell
Press: Enter
```

### STEP 2️⃣: Navigate to Project
```powershell
cd D:\deepak\dev\learning\projects\java-uml-generator
```

### STEP 3️⃣: Build (First Time Only)
```powershell
mvn clean compile

⏳ Wait for: [INFO] BUILD SUCCESS
```

### STEP 4️⃣: Choose Your Test File
```
Sample files in: src/samples/
- Order.java
- CreditCardPayment.java
- PaymentService.java
- IPaymentGateway.java
- PaymentProcessor.java
```

### STEP 5️⃣: Run the Tool
```powershell
# Option A: Text
.\run-uml.ps1 src/samples/Order.java

# Option B: Mermaid
.\run-uml-diagram.ps1 src/samples/CreditCardPayment.java mermaid

# Option C: PlantUML
.\run-uml-diagram.ps1 src/samples/PaymentService.java plantuml
```

### STEP 6️⃣: View Results
```
✅ Results appear in terminal
✅ Can copy to file
✅ Can paste into documentation
✅ Mermaid renders in GitHub!
```

---

## File Structure for Testing

```
Your Java File Location Matters!
│
├─ Option A: Place in src/samples/
│  └─ .\run-uml.ps1 src/samples/YourFile.java
│
├─ Option B: Use full path
│  └─ .\run-uml.ps1 C:\path\to\YourFile.java
│
├─ Option C: Use relative path
│  └─ .\run-uml.ps1 ..\other\project\YourFile.java
```

---

## Expected Output Timeline

```
⏱️ 0.0s  → Run command
⏱️ 0.5s  → Parse file
⏱️ 0.8s  → Analyze AST
⏱️ 1.0s  → Generate output
⏱️ 1.0s  → Display on screen
   
   TOTAL: ~1 second
```

---

## Success Indicators ✅

When things work correctly, you'll see:

✅ No errors in output  
✅ Class name appears  
✅ Package is shown  
✅ Fields listed with types  
✅ Methods listed with signatures  
✅ Relationships detected (if any)  

---

## Common Mistakes ❌ → ✅

| Mistake | Solution |
|---------|----------|
| Command not found | Check you're in project root |
| File not found | Verify file path is correct |
| Build fails | Run `mvn clean compile` first |
| No output | Check Java file has valid syntax |
| Wrong format | Use: `mermaid` or `plantuml` |

---

## PowerShell Command Anatomy

```
.\run-uml-diagram.ps1  src/samples/Order.java  mermaid
│                      │                       │
│                      │                       └─ Format
│                      └─ File Path
└─ Script Name
```

---

## Next Actions

After successful test:

```
1. ✅ Test worked!
   └─ Copy Mermaid to GitHub README
      └─ Commit and push
         └─ See diagram render!

2. 🔄 Test failed?
   └─ Check Java syntax
   └─ Verify file path
   └─ Read TESTING_GUIDE.md

3. 🚀 Ready for more?
   └─ Read PHASE2_SUMMARY.md (relationships)
   └─ Read PHASE3_SUMMARY.md (diagrams)
```

---

**Ready to test? Pick a command and paste it now!** 🚀

```powershell
.\run-uml.ps1 src/samples/Order.java
```

That's all you need! 🎉
