@echo off
echo ========================================
echo CURSOR STL Processor for Berg Mini Disc
echo ========================================
echo.

echo Installing Python dependencies...
pip install -r requirements.txt

echo.
echo Running STL processor...
python stl_processor.py

echo.
echo Processing complete! Check the CURSOR_Output\BergMini folder.
pause
