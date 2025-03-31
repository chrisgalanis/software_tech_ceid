# software_tech_ceid


### Github for Noobs

Welcome to Git! This guide is for beginners to understand the basic commands and keep the main branch safe. Let’s get started!
What You’ll Learn
  - How to grab a project (clone).
  - How to check what’s happening (status).
  - How to work with branches (not mess up main).
  - How to save and share your changes.

#### GOLDEN RULES:
Keep main Safe!
   - Don’t work on main: Always make a new branch with git checkout -b my-branch.
   - Check before pushing: Use git status to make sure you’re not on main.
   - Push to your branch: Use git push origin your-branch, not main.
   - Create PR: with good title and explanation of your changes.
   - Wait your changes to be reviewed and then merged :)

Quick Workflow - How you work on your branch
  - Clone the repo: git clone <url> (only done once)
  - Make a branch: git checkout -b my-feature
  - Add changes: git add .
  - Commit: git commit -m "did stuff"
  - Push: git push origin my-branch


#### Commands to Know
Clone a Repo (Get the Project)

    git clone <repo-url>

    Example: git clone https://github.com/user/repo.git

Check Where You Are
    
    git status

    Shows what branch you’re on and what’s changed.

See Branches
  
    git branch

    Lists all branches. The one with a * is where you are.

Switch Branches
  
    git checkout <branch-name>

    Moves you to another branch.
    Example: git checkout chris/<feature>

Make a New Branch
    
    git checkout -b <branch-name>

    Creates and switches to a new branch.
    Example: git checkout -b my-cool-feature

Add Changes
    
    git add <file>` or `git add -a

    Example: git add readme.md 

Save Changes (Commit) bash
    
    git commit -m "your message" 
  
    Saves your changes with a note.

git push origin <branch-name>
    
    Sends your branch to the remote repo (not main!).
  
    Example: git push origin my-cool-feature

Pull Updates
  
    git pull origin <branch-name>

    Grabs updates from the remote branch.
    Example: git pull origin main (only if you’re allowed!)

Now you’re a Git noob no more! Happy coding! 🚀

