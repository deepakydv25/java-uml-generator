# Quick Cheat Sheet - Java UML Generator

## ⚡ 30-Second Quick Start

```powershell
# 1. Navigate to project
cd D:\deepak\dev\learning\projects\java-uml-generator

# 2. Build (first time only)
mvn clean compile

# 3. Test your Java file
.\run-uml.ps1 src/samples/Order.java
```

That's it! You get instant UML output. 🚀

---

## 📋 Command Cheat Sheet

### Text Output (Most Readable)
```powershell
.\run-uml.ps1 <path-to-file>
```

**Example**:
```powershell
.\run-uml.ps1 src/samples/Order.java
.\run-uml.ps1 src/samples/CreditCardPayment.java
.\run-uml.ps1 src/samples/PaymentService.java
```

---

### Mermaid Diagram (GitHub-Ready)
```powershell
.\run-uml-diagram.ps1 <path-to-file> mermaid
```

**Example**:
```powershell
.\run-uml-diagram.ps1 src/samples/CreditCardPayment.java mermaid
```

**Output** → Paste into GitHub README:
```markdown
```mermaid
[paste here]
```
```

---

### PlantUML Diagram (Professional)
```powershell
.\run-uml-diagram.ps1 <path-to-file> plantuml
```

**Example**:
```powershell
.\run-uml-diagram.ps1 src/samples/PaymentService.java plantuml
```

---

### Save to File
```powershell
.\run-uml-diagram.ps1 <file> mermaid | Out-File diagram.mmd
```

---

## 🧪 Test Files Available

Ready to use in `src/samples/`:

```
Order.java                 - Simple class
PaymentProcessor.java      - Abstract base class
CreditCardPayment.java     - Inheritance example
IPaymentGateway.java       - Interface definition
PaymentService.java        - Interface implementation
```

**Quick test**:
```powershell
.\run-uml.ps1 src/samples/CreditCardPayment.java
```

---

## 🎯 Common Scenarios

### Scenario 1: Analyze Your Own Class
```powershell
# Copy your file to src/samples/
cp C:\path\to\YourClass.java src\samples\

# Then run
.\run-uml.ps1 src/samples/YourClass.java
```

---

### Scenario 2: Generate Diagram for GitHub
```powershell
# Generate Mermaid
.\run-uml-diagram.ps1 src/samples/YourClass.java mermaid

# Copy output and paste into README.md:
# ```mermaid
# [paste here]
# ```
```

---

### Scenario 3: Test Multiple Files
```powershell
# Test all samples
.\run-uml.ps1 src/samples/Order.java
.\run-uml.ps1 src/samples/CreditCardPayment.java
.\run-uml.ps1 src/samples/PaymentService.java
```

---

## 📊 Output Formats at a Glance

| Format | Use Case | Best For |
|--------|----------|----------|
| **Text** | Quick review | Understanding structure |
| **Mermaid** | GitHub docs | Rendering in README |
| **PlantUML** | Professional | Detailed diagrams |

---

## 🔨 Troubleshooting Quick Fix

| Problem | Solution |
|---------|----------|
| "File not found" | Check path is correct, file exists |
| "Build failed" | Run `mvn clean compile` |
| "Script not found" | Make sure you're in project root |
| "Parse error" | Check Java file syntax |

---

## 💾 Build & Setup (One Time Only)

```powershell
# Navigate to project
cd D:\deepak\dev\learning\projects\java-uml-generator

# Build the project (first time only)
mvn clean compile

# Now you can run any command!
```

---

## 🎓 What It Detects

✅ Classes, interfaces, enums, annotations  
✅ Fields (private, public, protected)  
✅ Methods (with parameters and return types)  
✅ Inheritance (extends)  
✅ Interface implementation (implements)  

---

## 📖 Full Documentation

| Document | Content |
|----------|---------|
| `README.md` | Complete guide |
| `QUICK_REFERENCE.md` | Executive summary |
| `TESTING_GUIDE.md` | Detailed testing instructions |
| `SAMPLE_DIAGRAMS.md` | Example diagrams |
| `PHASE2_SUMMARY.md` | Relationship detection |
| `PHASE3_SUMMARY.md` | Diagram generation |

---

## 🚀 Try It Right Now!

```powershell
cd D:\deepak\dev\learning\projects\java-uml-generator
.\run-uml.ps1 src/samples/Order.java
```

**Expected output**:
```
Class: Order
Package: com.example
Type: CLASS

Fields:
  - gateway : PaymentGateway
  - amount : double
  - orderId : String

Methods:
  + checkout() : void
  + setAmount(amount : double) : void
  + getAmount() : double
```

✨ **Success!** 🎉

---

## 📞 Quick Help

**Q: Where's my diagram?**  
A: Run with `mermaid` format and copy the output

**Q: How do I use it?**  
A: Run `TESTING_GUIDE.md` for complete instructions

**Q: Can I test my own files?**  
A: Yes! Copy to `src/samples/` and run the command

**Q: What about dependencies?**  
A: Coming in Phase 4

---

## 🎯 Next Steps

1. ✅ Build: `mvn clean compile`
2. ✅ Test: `.\run-uml.ps1 src/samples/Order.java`
3. ✅ Explore: Try different formats
4. ✅ Use: Copy diagrams to your projects!

---

**Keep it simple, keep it working!** 🚀
