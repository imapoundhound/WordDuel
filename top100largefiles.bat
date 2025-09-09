Add-Type -AssemblyName System.Windows.Forms
Add-Type -AssemblyName System.Drawing

$logFile = "R:\Temp\file_move_log.csv"

# Create form
$form = New-Object System.Windows.Forms.Form
$form.Text = "Top 100 Largest Files on C:\"
$form.Size = New-Object System.Drawing.Size(1000,650)
$form.StartPosition = "CenterScreen"

# Create ListView
$listView = New-Object System.Windows.Forms.ListView
$listView.View = 'Details'
$listView.CheckBoxes = $true
$listView.FullRowSelect = $true
$listView.Size = New-Object System.Drawing.Size(960,480)
$listView.Location = New-Object System.Drawing.Point(10,10)

$listView.Columns.Add("File Name", 250)
$listView.Columns.Add("Path", 400)
$listView.Columns.Add("Size (MB)", 100)
$listView.Columns.Add("Last Modified", 150)

# Get top 100 largest files
$files = Get-ChildItem -Path C:\ -Recurse -File -ErrorAction SilentlyContinue |
    Sort-Object Length -Descending |
    Select-Object -First 100

foreach ($file in $files) {
    $item = New-Object System.Windows.Forms.ListViewItem($file.Name)
    $item.SubItems.Add($file.DirectoryName)
    $item.SubItems.Add("{0:N2}" -f ($file.Length / 1MB))
    $item.SubItems.Add($file.LastWriteTime.ToString("yyyy-MM-dd HH:mm"))
    $item.Tag = $file.FullName
    $listView.Items.Add($item)
}

$form.Controls.Add($listView)

# Select All button
$selectAllBtn = New-Object System.Windows.Forms.Button
$selectAllBtn.Text = "Select All"
$selectAllBtn.Size = New-Object System.Drawing.Size(100,30)
$selectAllBtn.Location = New-Object System.Drawing.Point(10,500)
$selectAllBtn.Add_Click({
    foreach ($item in $listView.Items) {
        $item.Checked = $true
    }
})
$form.Controls.Add($selectAllBtn)

# Move to R:\Temp button
$deleteBtn = New-Object System.Windows.Forms.Button
$deleteBtn.Text = "Move Selected to R:\Temp"
$deleteBtn.Size = New-Object System.Drawing.Size(200,30)
$deleteBtn.Location = New-Object System.Drawing.Point(120,500)
$deleteBtn.Add_Click({
    $targetFolder = "R:\Temp"
    if (!(Test-Path $targetFolder)) {
        New-Item -Path $targetFolder -ItemType Directory | Out-Null
    }
    foreach ($item in $listView.CheckedItems) {
        $source = $item.Tag
        $destination = Join-Path $targetFolder ([System.IO.Path]::GetFileName($source))
        try {
            Move-Item -Path $source -Destination $destination -Force
            "$source,$destination,$([DateTime]::Now)" | Out-File -Append -FilePath $logFile
        } catch {
            [System.Windows.Forms.MessageBox]::Show("Failed to move: $source`n$_", "Error", [System.Windows.Forms.MessageBoxButtons]::OK, [System.Windows.Forms.MessageBoxIcon]::Error)
        }
    }
    [System.Windows.Forms.MessageBox]::Show("Selected files moved to R:\Temp", "Done", [System.Windows.Forms.MessageBoxButtons]::OK, [System.Windows.Forms.MessageBoxIcon]::Information)
})
$form.Controls.Add($deleteBtn)

# Restore button
$restoreBtn = New-Object System.Windows.Forms.Button
$restoreBtn.Text = "Restore Files from Log"
$restoreBtn.Size = New-Object System.Drawing.Size(200,30)
$restoreBtn.Location = New-Object System.Drawing.Point(330,500)
$restoreBtn.Add_Click({
    if (Test-Path $logFile) {
        $entries = Get-Content $logFile
        foreach ($entry in $entries) {
            $parts = $entry -split ","
            $original = $parts[0]
            $moved = $parts[1]
            try {
                Move-Item -Path $moved -Destination $original -Force
            } catch {
                [System.Windows.Forms.MessageBox]::Show("Failed to restore: $moved`n$_", "Error", [System.Windows.Forms.MessageBoxButtons]::OK, [System.Windows.Forms.MessageBoxIcon]::Error)
            }
        }
        Remove-Item $logFile -Force
        [System.Windows.Forms.MessageBox]::Show("Files restored to original locations", "Restored", [System.Windows.Forms.MessageBoxButtons]::OK, [System.Windows.Forms.MessageBoxIcon]::Information)
    } else {
        [System.Windows.Forms.MessageBox]::Show("No log file found for restoration.", "Missing Log", [System.Windows.Forms.MessageBoxButtons]::OK, [System.Windows.Forms.MessageBoxIcon]::Warning)
    }
})
$form.Controls.Add($restoreBtn)

# Show form
$form.Topmost = $true
$form.Add_Shown({$form.Activate()})
[void]$form.ShowDialog()