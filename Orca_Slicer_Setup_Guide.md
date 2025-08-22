# CURSOR Orca Slicer Setup Guide v1.00
## Berg-Style Mini Disc Marker - Multi-Material Printing

### 🎯 **Project Overview**
This guide will help you set up Orca Slicer for printing the Berg-Style Mini Disc Marker with multi-material capabilities:
- **Bottom 40%**: TPU Clear (thick base)
- **Middle 20%**: Flashforge Black Flexible (jellyfish design)
- **Top 40%**: TPU Clear (thick, rounded rim)

---

## 📋 **Prerequisites**
- Orca Slicer installed (latest version recommended)
- TPU Clear filament
- Flashforge Black Flexible filament
- 3D printer with dual extruder or manual filament change capability
- Generated STL/3MF files from `stl_processor.py`

---

## 🚀 **Step 1: Import Filament Profiles**

### Import TPU Clear Profile
1. Open Orca Slicer
2. Go to **File** → **Import** → **Import Config Bundle**
3. Select `TPU_Clear_Orca_Profile.ini`
4. Verify the profile appears in **Filament** settings

### Import Flashforge Black Profile
1. Repeat the import process for `Flashforge_Flexible_Black_Orca_Profile.ini`
2. Both profiles should now be available in your filament library

---

## ⚙️ **Step 2: Configure Multi-Material Settings**

### Enable Multi-Material Printing
1. In **Print Settings** → **Multiple Extruders**
2. Set **Extruders** to **2**
3. Enable **Ooze Shield** (recommended for TPU)

### Configure Extruder Assignments
1. **Extruder 1**: TPU Clear (for bottom and top layers)
2. **Extruder 2**: Flashforge Black Flexible (for middle layers)

---

## 🎨 **Step 3: Import and Prepare Model**

### Load the STL/3MF File
1. **File** → **Open** → Select your generated file:
   - `CURSOR_OUTPUT_version1.00_berg_mini_disc.stl` or
   - `CURSOR_OUTPUT_version1.00_berg_mini_disc.3mf`

### Position and Scale
1. Ensure model is centered on build plate
2. Verify orientation (flat side down)
3. Check dimensions match your requirements

---

## 🔧 **Step 4: Configure Layer-Based Material Assignment**

### Set Extruder by Height
1. **Print Settings** → **Multiple Extruders** → **Set Extruder based on height**
2. Configure the following rules:

```
Layer Height Rules:
- 0.0mm to 40% of total height: Extruder 1 (TPU Clear)
- 40% to 60% of total height: Extruder 2 (Flashforge Black)
- 60% to 100% of total height: Extruder 1 (TPU Clear)
```

### Example Configuration
If your model is 10mm tall:
- **0.0mm - 4.0mm**: TPU Clear (Extruder 1)
- **4.0mm - 6.0mm**: Flashforge Black (Extruder 2)
- **6.0mm - 10.0mm**: TPU Clear (Extruder 1)

---

## 🖨️ **Step 5: Print Settings Optimization**

### Quality Settings
- **Layer Height**: 0.2mm
- **First Layer Height**: 0.3mm
- **Perimeters**: 3-4
- **Top/Bottom Layers**: 4-5
- **Infill Density**: 30-35%

### Speed Settings
- **Print Speed**: 25-30 mm/s (TPU), 20-25 mm/s (Flexible)
- **Travel Speed**: 60-80 mm/s
- **First Layer Speed**: 15-20 mm/s

### Cooling Settings
- **Fan Speed**: 50-60%
- **Bridge Fan Speed**: 80-100%
- **Enable Cooling**: Yes

---

## 🔄 **Step 6: Filament Change Configuration**

### Manual Filament Changes
If using single extruder with manual changes:

1. **Custom G-code** → **Before Layer Change**
2. Add at 40% height:
   ```gcode
   ; Filament Change 1: TPU Clear → Flashforge Black
   M600 T0 ; Filament change command
   ; Remove TPU Clear, insert Flashforge Black
   ; Press continue when ready
   ```

3. Add at 60% height:
   ```gcode
   ; Filament Change 2: Flashforge Black → TPU Clear
   M600 T1 ; Filament change command
   ; Remove Flashforge Black, insert TPU Clear
   ; Press continue when ready
   ```

### Automatic Filament Changes (Dual Extruder)
1. Enable **Wipe Tower**
2. Set **Wipe Tower Width**: 60mm
3. Configure **Wipe Tower Pattern**: **Concentric**

---

## 📊 **Step 7: Slice and Preview**

### Generate G-code
1. Click **Slice Now**
2. Wait for processing to complete
3. Review the **Preview** tab

### Verify Material Distribution
1. Check **Layers** view
2. Verify correct extruder assignment for each height range
3. Look for proper transitions between materials

---

## 🎯 **Step 8: Print Preparation**

### Pre-print Checklist
- [ ] TPU Clear loaded in Extruder 1
- [ ] Flashforge Black loaded in Extruder 2 (if dual extruder)
- [ ] Bed properly leveled
- [ ] Nozzle clean and clear
- [ ] Filament paths clear of obstructions

### Temperature Settings
- **TPU Clear**: 220°C nozzle, 60°C bed
- **Flashforge Black**: 230°C nozzle, 65°C bed

---

## 🚨 **Troubleshooting**

### Common Issues

#### Stringing Between Materials
- Increase retraction distance
- Reduce print temperature
- Increase travel speed

#### Poor Layer Adhesion
- Increase first layer temperature
- Reduce first layer speed
- Check bed leveling

#### Filament Change Failures
- Ensure M600 commands are properly placed
- Check printer firmware supports M600
- Verify filament is properly loaded

#### TPU Adhesion Issues
- Use brim (5-8mm width)
- Increase first layer extrusion width
- Ensure bed is clean and properly heated

---

## 📈 **Expected Results**

### Print Quality
- **Surface Finish**: Smooth with minimal stringing
- **Layer Adhesion**: Strong between all materials
- **Dimensional Accuracy**: ±0.2mm tolerance
- **Print Time**: 2-3 hours (depending on settings)

### Material Properties
- **TPU Clear**: Flexible, transparent, durable
- **Flashforge Black**: Flexible, black, embedded design
- **Overall**: Berg-style disc golf putter shape with fat, rounded rim

---

## 🔄 **Version Updates**

### Current Version: 1.00
- Initial Berg-style design implementation
- Multi-material layer distribution (40/20/40)
- Optimized profiles for both materials

### Future Updates
- Enhanced material transition algorithms
- Additional design variations
- Improved print quality optimizations

---

## 📞 **Support**

If you encounter issues:
1. Check the troubleshooting section above
2. Verify all settings match the provided profiles
3. Ensure your printer supports the required features
4. Review the generated G-code for errors

---

## 🎉 **Success!**

Once complete, you'll have a beautiful Berg-style mini disc marker with:
- Sturdy TPU Clear base and rim
- Embedded Flashforge Black jellyfish design
- Professional disc golf putter aesthetics
- Multi-material durability and flexibility

**Happy Printing! 🖨️⛳**
