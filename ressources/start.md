# Restaurant Reservation System - Getting Started 🚀

**Important:** Read this file completely before starting your project.

---

## 📋 Quick Overview

You will:

1. ✅ Accept the Gitee invitation (within 48 hours)
2. ✅ Clone the project repository
3. ✅ Create your personal branch (your student ID)
4. ✅ Design your system (on paper first!)
5. ✅ Implement your solution
6. ✅ Test thoroughly
7. ✅ Document your work
8. ✅ Push your completed work

---

## 🎯 Step-by-Step Instructions

### Step 1: Accept Gitee Invitation ⏰

**Deadline: Within 48 hours of receiving the invitation**

1. Click on the invitation link
2. Accept the invitation to join the project repository

⚠️ **CRITICAL:** If you don't accept within 48 hours, you will lose points!

---

### Step 2: Clone the Repository 💻

Once you've accepted the invitation:

```bash
# Clone the project repository
git clone git@gitee.com:professor_baptiste/java-final-project.git

# Navigate into the repository folder
cd java-final-project
```

**Note:** `java-final-project` is the Git repository. Your Spring Boot project will be created inside this folder.

---

### Step 3: Create Your Personal Branch 🌿

**IMPORTANT:** Each student must work on their own branch named with their student ID.

```bash
# Create and switch to your branch (replace with YOUR actual student ID)
git checkout -b YOUR-STUDENT-ID

# Example: If your student ID is "202300000"
git checkout -b 202300000
```

**Branch Naming Rules:**

- ✅ Use ONLY your student ID number
- ✅ No prefix, no suffix, just the ID
- ❌ NOT `student-202300000` or `202300000-final`

---

### Step 4: Create Your Spring Boot Project 💪

**Option A: Using Spring Initializr (Recommended)**

1. Go to [https://start.spring.io/](https://start.spring.io/)
2. Configure:
   - Project: Maven
   - Language: Java
   - Spring Boot: 3.x (latest stable)
   - Group: `com.restaurant`
   - Artifact: `reservation-api`
   - Packaging: Jar
   - Java: 21
3. Add Dependencies:
   - Spring Web
   - Spring Data JPA
   - H2 Database
   - Validation
4. Generate and download
5. Extract into your repository folder

**Option B: Using IDE**

Create a new Spring Boot project with the same configuration in your IDE.

---

### Step 5: Design Before You Code! 📝

**STOP! Don't write any code yet.**

Before touching the keyboard:

1. **Draw your entity diagram**

   - What entities do you need?
   - What are the relationships?
   - What fields does each entity have?

2. **Plan your API**

   - What endpoints will you create?
   - What HTTP methods?
   - What request/response formats?

3. **Think about business rules**
   - How do you prevent double-booking?
   - How do you check availability?
   - What status transitions are valid?

**If you can't draw it, you can't build it.**

---

### Step 6: Commit Regularly! 📝

**🔴 CRITICAL: Your commits are part of your grade (15 points)!**

### Why Commits Matter

Professor Baptiste will review:

- ✅ Your commit frequency (minimum 10 commits recommended)
- ✅ Your commit messages (must be in English)
- ✅ Project evolution through commits

### Good Commit Practices

```bash
# After creating entity classes
git add .
git commit -m "Add Restaurant and Table entities with JPA mapping"

# After implementing a service
git add .
git commit -m "Implement ReservationService with availability check"

# After fixing a bug
git add .
git commit -m "Fix double-booking validation in createReservation"

# After adding exception handling
git add .
git commit -m "Add GlobalExceptionHandler for custom exceptions"
```

### Commit Message Rules

**✅ GOOD Commit Messages (English):**

- `"Add Restaurant entity with validation"`
- `"Implement availability checking logic"`
- `"Fix bug in time slot overlap detection"`
- `"Add DTOs for reservation requests"`
- `"Create GlobalExceptionHandler"`

**❌ BAD Commit Messages:**

- `"update"` (not descriptive)
- `"添加代码"` (not in English)
- `"asdfasdf"` (meaningless)
- `"final"` (not descriptive)
- `"changes"` (too vague)

### How Often Should You Commit?

**Commit after:**

- ✅ Creating a new class
- ✅ Implementing a feature
- ✅ Fixing a bug
- ✅ Adding validation
- ✅ Any working checkpoint

**Rule of thumb:** If you've done something meaningful that works, commit it!

---

### Step 7: Push Your Work to Gitee 🚀

After each coding session, push your commits:

```bash
# Check what branch you're on
git branch

# Push your branch to Gitee
git push origin YOUR-STUDENT-ID

# Example:
git push origin 202300000
```

**Push frequently:**

- At least once per day when working
- After completing major features
- Before taking a break
- When you're done for the day

---

### Step 8: Create Your README.md 📄

Your project MUST include a README.md with:

```markdown
# Restaurant Reservation API

## Author

- Name: [Your Name]
- Student ID: [Your ID]

## Project Description

Brief description of what your API does.

## How to Run

1. Clone the repository
2. Navigate to project folder
3. Run: `mvn spring-boot:run`
4. API available at: http://localhost:8080

## API Endpoints

### Restaurants

| Method | Endpoint         | Description         |
| ------ | ---------------- | ------------------- |
| GET    | /api/restaurants | Get all restaurants |
| ...    | ...              | ...                 |

### Reservations

| Method | Endpoint          | Description        |
| ------ | ----------------- | ------------------ |
| POST   | /api/reservations | Create reservation |
| ...    | ...               | ...                |

## Design Decisions

Explain your key architectural choices:

- Why did you structure entities this way?
- How does availability checking work?
- What status flow did you implement?

## Challenges & Solutions

What problems did you encounter and how did you solve them?
```

---

## ⚠️ Important Warnings

### 1. Never Work on the `main` Branch ❌

```bash
# ❌ WRONG - Don't do this!
git checkout main
git commit -m "my changes"

# ✅ CORRECT - Always work on your branch
git checkout 202300000
git commit -m "Add ReservationService"
```

### 2. Don't Forget .gitignore 📋

Create a `.gitignore` file:

```gitignore
# Maven
target/
*.jar

# IDE
.idea/
*.iml
.vscode/
.DS_Store

# Build
build/
out/

# Logs
*.log
```

### 3. Commit Messages Must Be in English 🇬🇧

**Why?** Professional software development uses English.

### 4. Don't Wait Until the Last Day ⏰

**❌ BAD:**

```
Day 1-6: [no commits]
Day 7: "Complete project" (1 commit)
```

**✅ GOOD:**

```
Day 1: "Initial Spring Boot setup"
Day 1: "Add entity classes"
Day 2: "Implement repositories"
Day 2: "Add RestaurantService"
Day 3: "Add ReservationService with validation"
...
Day 7: "Final testing and documentation"
```

---

## 🎯 Complete Workflow Example

```bash
# Day 1: Setup
git clone [repository-url]
cd java-final-project
git checkout -b 202300000

# Create Spring Boot project
# Add to .gitignore
git add .
git commit -m "Initial Spring Boot project setup"
git push origin 202300000

# Day 2: Entities
# Create Restaurant.java, Table.java, etc.
git add .
git commit -m "Add core entity classes with JPA mapping"
git push origin 202300000

# Day 3: Repositories and Services
# Create repositories and business logic
git add .
git commit -m "Implement ReservationService with availability check"
git push origin 202300000

# Day 4: Controllers and DTOs
# Create API endpoints
git add .
git commit -m "Add ReservationController with full CRUD"
git push origin 202300000

# Day 5: Exception handling
git add .
git commit -m "Add GlobalExceptionHandler and custom exceptions"
git push origin 202300000

# Day 6: Testing and refinement
# Create api-tests.http
git add .
git commit -m "Add comprehensive API tests"
git push origin 202300000

# Day 7: Documentation
# Write README.md
git add .
git commit -m "Add complete README documentation"
git push origin 202300000
```

---

## 📊 Grading Related to Git (15 Points Total)

**Git Workflow & Version Control: 15 points**

- **Commit Frequency:**

  - 10+ commits: Full credit
  - 7-9 commits: Partial credit
  - 4-6 commits: Low credit
  - <4 commits: Minimal credit

- **Commit Quality:**

  - All messages in English: Required
  - Messages are clear and descriptive: Required

- **Project Evolution:**
  - Professor can follow your development
  - Commits show logical progression
  - Each commit represents meaningful work

---

## ✅ Pre-Submission Checklist

### Git Checklist

- [ ] Working on my personal branch (my student ID)
- [ ] At least 10 meaningful commits
- [ ] All commit messages in English
- [ ] All commit messages are descriptive
- [ ] Pushed all commits to Gitee
- [ ] .gitignore is configured

### Code Checklist

- [ ] Project compiles without errors
- [ ] All required features implemented
- [ ] Business rules enforced
- [ ] Clean Code principles followed
- [ ] Exception handling complete
- [ ] Validation working

### Documentation Checklist

- [ ] README.md written
- [ ] API endpoints documented
- [ ] Design decisions explained
- [ ] api-tests.http included

### Final Check

- [ ] Read the grading rubric
- [ ] Tested all scenarios
- [ ] Pushed final version before deadline

---

## 🆘 Common Issues & Solutions

### Issue 1: "I can't push to Gitee"

**Possible causes:**

1. Didn't accept invitation → Go accept it
2. Wrong repository URL → Check with professor
3. Not on your branch → Run `git checkout YOUR-STUDENT-ID`
4. Authentication issue → Configure Git credentials

### Issue 2: "I made commits with Chinese messages"

**Solution:**

```bash
# Change last commit message
git commit --amend -m "New message in English"
git push origin YOUR-STUDENT-ID --force
```

⚠️ Only do this for recent commits!

### Issue 3: "I accidentally worked on main branch"

**Solution:**

```bash
# Create your branch from current position
git checkout -b YOUR-STUDENT-ID
git push origin YOUR-STUDENT-ID
```

### Issue 4: "My project doesn't start"

**Check:**

1. Java 21 installed?
2. Maven installed?
3. Dependencies downloaded? (`mvn clean install`)
4. application.properties configured?

---

## 📞 Need Help?

**If you're stuck:**

1. Check this README again
2. Review the hints document
3. Search online
4. Ask classmates
5. Contact professor Baptiste

**Remember:** Make sure you understand everything you code - your commits and code quality reflect your learning!

---

## 🎓 Summary

**The Essential Steps:**

1. ✅ **Accept invitation** (within 48 hours)
2. ✅ **Create your branch** (your student ID)
3. ✅ **Design first** (on paper!)
4. ✅ **Code incrementally** (commit often)
5. ✅ **Test thoroughly** (happy paths + errors)
6. ✅ **Document your work** (README.md)
7. ✅ **Push everything** (before deadline)

**Your commits tell the story of how you built the project.**

**Make it a professional story! 📖**

---

**Good luck with your final project! 🚀💪**
