; CURSOR_ Multi-Material Jellyfish Disc - Berg-Style
; Filament Change Instructions

; Layer 1-40% (Bottom): TPU Clear
; Start with TPU Clear filament
M104 S220 T0 ; Set TPU Clear temperature
M109 S220 T0 ; Wait for TPU Clear temperature

; Print bottom layers (approximately 40% of total height)
; ... printing continues ...

; FILAMENT CHANGE 1: Switch to Flashforge Flexible Black
M600 ; Filament change command
; Remove TPU Clear, insert Flashforge Flexible Black
; Press continue when ready

; Layer 41-60% (Middle): Flashforge Flexible Black
M104 S230 T1 ; Set Flashforge Black temperature
M109 S230 T1 ; Wait for Flashforge Black temperature

; Print middle section with jellyfish design (20% of height)
; ... printing continues ...

; FILAMENT CHANGE 2: Switch back to TPU Clear
M600 ; Filament change command
; Remove Flashforge Black, insert TPU Clear
; Press continue when ready

; Layer 61-100% (Top): TPU Clear
M104 S220 T0 ; Set TPU Clear temperature
M109 S220 T0 ; Wait for TPU Clear temperature

; Print top layers (40% of height) - thick, rounded top rim
; ... printing continues ...

; Print complete!
M104 S0 ; Turn off heaters
M84 ; Disable steppers
