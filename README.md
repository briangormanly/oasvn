# oasvn
Open Android SVN - Functional SVN (Subversion) client for Android

The first functional SVN (Subversion) client for Android that correctly uses the SVN protocol! Works for http:// https:// and svn:// repositories! svn+ssh:// (and private key) has been added, please email me with input on it.
This is a completely free (and ad free) open source effort to bring the best svn client to the android platform! There are no limitations this free version, I just ask that you use the jira: https://openandroidsvn.atlassian.net/ to report any bugs or issues (also if you want to contribute to the project). The available Professional version receives bug fixes and general stability updates that the free version does not get and helps support development of OASVN. I will be adding additional nice features to the pro version only, but I want to keep the free version fully functional as an subversion client.

### Contributors 
Major Contributors to the project include: (Thank You!)
Tim Jagenberg : svn+ssh with private key
Sascha Zieger: local browser and export ability

### Features
This version of OASVN supports the following SVN operations:
• Checkout (Head or Choose Revision)
• Update
• Commit
• Add (files added to the working copy directory are automatically added on commit
• Export (allows complete export from the remote repo or just exporting just one file from either the working copy or the remote repository).
• Cleanup
• Revert
• Conflict Resolution!!!! (Notification of conflicts happens during update and conflicted files are marked in the local browser, clicking on the file in the local browser presents resolution options

Additional features:
• Remote Repository browse allows the ability to export any single file to your local file system
• Remote Revision list with complete details of files added/deleted/modified, author, commit comments, date, etc.
• Log keeps track of all successes and failures on a connection basis and retains information until you clear it out.
• Local Repository browsing allows you to export from your local copy, open files via intent by mime type (should present your with the correct application choices for the job, it is up to the application to respect opening the file by URI) and also shows svn status of files in conflict, or locally updated.
• New svn+ssh code with private key support! (again this is not well tested, I would appreciate feedback using the jira: https://openandroidsvn.atlassian.net/
• Now allows you to save working copies and exports anywhere on the local file-system. (the predetermined /mnt/sdcard/OASVN has been removed)

Also there is English and German language support. Translations in any language welcome!

We have taken every precaution to ensure this works correctly, but please make sure to back up your repositories regularly!

### Original source location: (archived to github)
Source for the project is located here: https://code.google.com/p/open-andriod-svn/
Jira: https://openandroidsvn.atlassian.net/

### Notes:
Note: svn+ssl is still experimental. I have received some positive feedback but I do not have access to do in-depth testing yet on this protocol, please send me any feedback using the Jira, my email or website.