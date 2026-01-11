# Budapest Community Budget

A PHP-based web application for managing community budget proposals, voting, and administration using JSON file storage.

## Features

- **Project Management**: Users can submit project proposals with categories, descriptions, and images
- **Voting System**: Logged-in users can vote on approved projects (max 3 votes per category, 1 per project)
- **Admin Panel**: Administrators can review, approve, reject, or send back projects for rework
- **Statistics**: Comprehensive statistics showing leading projects by category and user activity
- **2-Week Voting Window**: Each project has a 2-week voting period after publication
- **Session-Based Authentication**: Secure login/registration system
- **JSON Data Storage**: All data stored in lightweight JSON files - no database required!

## Technology Stack

- **Backend**: Pure PHP (no frameworks)
- **Frontend**: Pure HTML, CSS, JavaScript (no libraries)
- **Data Storage**: JSON files
- **AJAX**: Vanilla JavaScript Fetch API for asynchronous voting

## Installation

### Prerequisites

- PHP 7.4 or higher
- Apache/Nginx web server (or PHP built-in server)
- Write permissions for the data directory

### Setup Steps

1. **Clone/Download the project**
   ```
   Copy all files to your web server directory (e.g., htdocs, www, public_html)
   ```

2. **Set up permissions**
   - Ensure the `data/` directory has write permissions
   - On Linux/Mac: `chmod -R 755 data/`
   - On Windows: Right-click data folder → Properties → Security → Give write access

3. **Start the application**
   - If using Apache/Nginx, navigate to the project URL
   - Or use PHP's built-in server:
     ```bash
     cd assignment-gh
     php -S localhost:8000
     ```
   - Access at `http://localhost:8000`

4. **Default Admin Account**
   - Username: `admin`
   - Password: `admin123`
   - **Important**: Change this password after first login!

## Data Storage Structure

All data is stored in JSON files within the `data/` directory:

```
data/
├── users.json       # User accounts
├── categories.json  # Project categories
├── projects.json    # All project submissions
└── votes.json       # User votes
```

### Initial Data

The application comes pre-configured with:
- 1 admin user (admin/admin123)
- 6 project categories (Infrastructure, Education, Environment, Culture, Sports, Health)
- Empty projects and votes arrays

## File Structure

```
├── config.php              # JSON operations and helper functions
├── index.php              # Homepage with project listing and voting
├── login.php              # Login and registration page
├── logout.php             # Logout handler
├── project.php            # Individual project detail page
├── projects-own.php       # User's submitted projects
├── projects-admin.php     # Admin panel for pending projects
├── submit-project.php     # Project submission form
├── edit-project.php       # Edit project in rework status
├── statistics.php         # Statistics dashboard (admin only)
├── vote.php               # AJAX vote handler
├── script.js              # JavaScript for async voting
├── styles.css             # All CSS styles
├── data/                  # JSON data storage directory
│   ├── users.json
│   ├── categories.json
│   ├── projects.json
│   └── votes.json
└── README.md              # This file
```

## Usage

### For Regular Users

1. **Register/Login**: Click "Login / Register" button
2. **View Projects**: Browse published projects on the homepage
3. **Vote**: Click "Vote" button next to projects (max 3 per category)
4. **Submit Project**: Use "Submit Project" to propose a new idea
5. **Track Projects**: View your submissions in "My Projects"

### For Administrators

1. **Login** with admin credentials
2. **Review Projects**: Go to "Admin Panel" to see pending submissions
3. **Approve/Reject**: Click "Review" on any project to approve, reject, or send back for rework
4. **View Statistics**: Access comprehensive analytics in "Statistics"

## Project Workflow

1. **Submission**: User submits a project (status: `pending`)
2. **Review**: Admin reviews the project
3. **Options**:
   - **Approve**: Project is published and voting begins (status: `approved`)
   - **Reject**: Project is rejected (status: `rejected`)
   - **Rework**: Admin sends feedback, user can edit and resubmit (status: `rework`)
4. **Voting**: Users can vote for 2 weeks after publication
5. **Results**: View vote counts and statistics

## Voting Rules

- Must be logged in to vote
- Maximum 3 votes per category per user
- Only 1 vote per project per user
- Can vote on own projects
- Votes can be withdrawn during the 2-week period
- After 2 weeks, voting closes and votes are final

## Data Persistence

All data is automatically saved to JSON files in real-time:
- User registrations → `data/users.json`
- Project submissions → `data/projects.json`
- Votes → `data/votes.json`
- Categories are pre-configured in `data/categories.json`

### Backup & Reset

**To backup your data:**
```bash
cp -r data/ data_backup/
```

**To reset to initial state:**
```bash
# Restore the initial JSON files (will lose all user data)
```

## Security Features

- Password hashing with `password_hash()`
- HTML output sanitization
- Session-based authentication
- Access control for admin-only pages
- CSRF protection through session validation
- File locking for concurrent write operations

## Customization

- Add more categories by editing `data/categories.json`
- Modify styles in `styles.css`
- Adjust voting limits in vote logic
- Change 2-week voting period in `isVotingOpen()` function in `config.php`

## Troubleshooting

**Cannot write to JSON files:**
- Check write permissions on the `data/` directory
- Ensure PHP has permission to write files
- On Linux/Mac: `chmod -R 755 data/`

**Login not working:**
- Check session support in PHP
- Clear browser cookies
- Verify user exists in `data/users.json`

**Voting not working:**
- Check JavaScript console for errors
- Ensure `vote.php` is accessible
- Verify user is logged in
- Check that JSON files are writable

**Data corrupted:**
- Restore from backup if available
- Manually fix JSON syntax in data files
- Ensure valid JSON format (use JSON validator)

## Advantages of JSON Storage

✅ **No Database Required**: No MySQL setup, perfect for shared hosting  
✅ **Portable**: Copy entire directory, no database export/import needed  
✅ **Easy Backup**: Simple file copy for complete backup  
✅ **Human Readable**: Data files can be viewed and edited in any text editor  
✅ **Version Control Friendly**: Can track data changes with Git  
✅ **Quick Setup**: Works immediately, no configuration needed

## License

This project was created for educational purposes.

## Support

For issues or questions, please refer to the code comments or contact the development team.
