#!/usr/bin/env python3
"""
CURSOR STL Processor for Berg-Style Mini Disc Marker
Multi-material printing with Flashforge Black Flexible + TPU Clear
"""

import numpy as np
import struct
import os
import sys
from pathlib import Path

class BergMiniDiscProcessor:
    def __init__(self):
        self.output_dir = Path(r"R:\AppBuilds\source\WordDuelCursorProject\WordDuel_Projects\WordDuel_Too_Project\CURSOR_Output\BergMini")
        self.version = "1.00"
        self.prefix = f"CURSOR_OUTPUT_version{self.version}"
        
        # Material distribution for Berg-style design
        self.material_layers = {
            'bottom': 0.40,    # TPU Clear - 40% of height
            'middle': 0.20,    # Flashforge Black - 20% of height  
            'top': 0.40        # TPU Clear - 40% of height
        }
        
        # Create output directory
        self.output_dir.mkdir(parents=True, exist_ok=True)
        
    def process_stl(self, input_file):
        """Process the STL file and create multi-material versions"""
        print(f"Processing: {input_file}")
        
        # Read and parse STL
        vertices, faces = self.read_stl(input_file)
        
        # Fix non-manifold edges
        vertices, faces = self.fix_non_manifold(vertices, faces)
        
        # Create Berg-style modifications
        vertices = self.apply_berg_style(vertices)
        
        # Generate output files
        self.generate_stl(vertices, faces)
        self.generate_3mf(vertices, faces)
        self.generate_gcode_instructions()
        
        print(f"✅ Processing complete! Files saved to: {self.output_dir}")
        
    def read_stl(self, filepath):
        """Read STL file and return vertices and faces"""
        with open(filepath, 'rb') as f:
            # Skip header
            f.read(80)
            
            # Read triangle count
            triangle_count = struct.unpack('<I', f.read(4))[0]
            
            vertices = []
            faces = []
            
            for i in range(triangle_count):
                # Read normal and vertices
                data = f.read(50)  # 12 bytes normal + 36 bytes vertices + 2 bytes attribute
                normal = struct.unpack('<3f', data[0:12])
                v1 = struct.unpack('<3f', data[12:24])
                v2 = struct.unpack('<3f', data[24:36])
                v3 = struct.unpack('<3f', data[36:48])
                
                vertices.extend([v1, v2, v3])
                faces.append([i*3, i*3+1, i*3+2])
                
        return np.array(vertices), np.array(faces)
    
    def fix_non_manifold(self, vertices, faces):
        """Fix non-manifold edges in the mesh"""
        print("🔧 Fixing non-manifold edges...")
        
        # Simple approach: remove duplicate vertices
        unique_vertices, inverse_indices = np.unique(vertices, axis=0, return_inverse=True)
        fixed_faces = inverse_indices[faces]
        
        return unique_vertices, fixed_faces
    
    def apply_berg_style(self, vertices):
        """Apply Berg-style disc golf putter modifications"""
        print("⛳ Applying Berg-style disc golf putter shape...")
        
        # Get height range
        min_z = np.min(vertices[:, 2])
        max_z = np.max(vertices[:, 2])
        height = max_z - min_z
        
        # Calculate layer boundaries
        bottom_height = min_z + height * self.material_layers['bottom']
        middle_height = min_z + height * (self.material_layers['bottom'] + self.material_layers['middle'])
        
        modified_vertices = vertices.copy()
        
        for i, vertex in enumerate(vertices):
            z = vertex[2]
            x, y = vertex[0], vertex[1]
            
            # Calculate distance from center
            distance = np.sqrt(x*x + y*y)
            
            if z < bottom_height:
                # Bottom layers (TPU Clear) - keep original
                pass
            elif z < middle_height:
                # Middle layers (Flashforge Black) - jellyfish design
                # Slightly reduce height for design portion
                modified_vertices[i, 2] = z * 0.8
            else:
                # Top layers (TPU Clear) - Berg-style fat rounded rim
                # Make rim thicker and more rounded
                rim_factor = 1.0 + 0.3 * (z - middle_height) / (max_z - middle_height)
                modified_vertices[i, 0] = x * rim_factor
                modified_vertices[i, 1] = y * rim_factor
                modified_vertices[i, 2] = z * 1.1  # Slightly taller for rounded effect
        
        return modified_vertices
    
    def generate_stl(self, vertices, faces):
        """Generate modified STL file"""
        output_file = self.output_dir / f"{self.prefix}_berg_mini_disc.stl"
        
        with open(output_file, 'wb') as f:
            # Write header
            header = f"CURSOR Berg-Style Mini Disc Marker v{self.version}".encode('utf-8').ljust(80, b'\x00')
            f.write(header)
            
            # Write triangle count
            f.write(struct.pack('<I', len(faces)))
            
            # Write triangles
            for face in faces:
                # Calculate normal
                v1, v2, v3 = vertices[face]
                normal = np.cross(v2 - v1, v3 - v1)
                normal = normal / np.linalg.norm(normal)
                
                # Write normal
                f.write(struct.pack('<3f', *normal))
                
                # Write vertices
                f.write(struct.pack('<3f', *v1))
                f.write(struct.pack('<3f', *v2))
                f.write(struct.pack('<3f', *v3))
                
                # Write attribute
                f.write(struct.pack('<H', 0))
        
        print(f"📁 STL saved: {output_file}")
    
    def generate_3mf(self, vertices, faces):
        """Generate 3MF file with multi-material metadata"""
        output_file = self.output_dir / f"{self.prefix}_berg_mini_disc.3mf"
        
        # Create 3MF content (simplified version)
        content = f"""<?xml version="1.0" encoding="UTF-8"?>
<model unit="millimeter" xml:lang="en-US" xmlns="http://schemas.microsoft.com/3dmanufacturing/core/2015/02" xmlns:m="http://schemas.microsoft.com/3dmanufacturing/material/2015/02">
    <metadata name="Application">CURSOR STL Processor v{self.version}</metadata>
    <metadata name="Title">Berg-Style Mini Disc Marker</metadata>
    <metadata name="Description">Multi-material mini disc with jellyfish design and Berg-style rim</metadata>
    <metadata name="Material Distribution">Bottom 40% TPU Clear, Middle 20% Flashforge Black, Top 40% TPU Clear</metadata>
    <metadata name="Design Style">Berg Disc Golf Putter Shape</metadata>
    
    <resources>
        <object id="1" type="model">
            <mesh>
                <vertices>
"""
        
        # Add vertices
        for vertex in vertices:
            content += f"                    <vertex x=\"{vertex[0]:.6f}\" y=\"{vertex[1]:.6f}\" z=\"{vertex[2]:.6f}\" />\n"
        
        content += """                </vertices>
                <triangles>
"""
        
        # Add faces
        for face in faces:
            content += f"                    <triangle v1=\"{face[0]}\" v2=\"{face[1]}\" v3=\"{face[2]}\" />\n"
        
        content += """                </triangles>
            </mesh>
        </object>
        
        <m:basematerials>
            <m:basematerial id="1" name="TPU Clear" color="#FFFFFF" />
            <m:basematerial id="2" name="Flashforge Black Flexible" color="#000000" />
        </m:basematerials>
    </resources>
    
    <build>
        <item objectid="1" transform="1 0 0 0 1 0 0 0 1 0 0 0" />
    </build>
</model>"""
        
        with open(output_file, 'w', encoding='utf-8') as f:
            f.write(content)
        
        print(f"📁 3MF saved: {output_file}")
    
    def generate_gcode_instructions(self):
        """Generate G-code instructions for multi-material printing"""
        output_file = self.output_dir / f"{self.prefix}_printing_instructions.txt"
        
        instructions = f"""CURSOR Berg-Style Mini Disc Marker - Printing Instructions v{self.version}
================================================================

MATERIAL DISTRIBUTION:
- Bottom 40%: TPU Clear
- Middle 20%: Flashforge Black Flexible (Jellyfish Design)
- Top 40%: TPU Clear

PRINTING SEQUENCE:
1. Start with TPU Clear
2. At 40% height: Switch to Flashforge Black Flexible
3. At 60% height: Switch back to TPU Clear
4. Complete with TPU Clear

FILAMENT CHANGE COMMANDS (M600):
- Layer change at 40% height: M600 T0 (TPU Clear → Black)
- Layer change at 60% height: M600 T1 (Black → TPU Clear)

ORCA SLICER SETTINGS:
- Import provided TPU_Clear_Orca_Profile.ini
- Import provided Flashforge_Flexible_Black_Orca_Profile.ini
- Set layer height: 0.2mm
- Enable multi-material printing
- Use "Set Extruder based on height" feature

ESTIMATED PRINT TIME: 2-3 hours
SUPPORT: None required
INFILL: 20-30%
WALLS: 3-4 perimeters

NOTES:
- Berg-style design features fat, rounded rim like disc golf putter
- Jellyfish design is embedded in middle layers
- Ensure proper bed adhesion for TPU materials
- Monitor first few layers carefully

For detailed setup guide, see: Orca_Slicer_Setup_Guide.md
"""
        
        with open(output_file, 'w', encoding='utf-8') as f:
            f.write(instructions)
        
        print(f"📁 Instructions saved: {output_file}")

def main():
    print("🚀 CURSOR STL Processor for Berg-Style Mini Disc Marker")
    print("=" * 60)
    
    processor = BergMiniDiscProcessor()
    
    # Check if input file exists
    input_file = r"R:\3DpRINTjOBS\MINIDisc\jellyfish\mini-marker-meduse20200507-5106-i1jltk\Bureau_B\mini-marker-meduse\minmeduzfin.stl"
    
    if not os.path.exists(input_file):
        print(f"❌ Input file not found: {input_file}")
        print("Please ensure the STL file is available at the specified path.")
        return
    
    try:
        processor.process_stl(input_file)
        print("\n🎉 All files generated successfully!")
        print(f"📂 Output location: {processor.output_dir}")
        
    except Exception as e:
        print(f"❌ Error during processing: {str(e)}")
        import traceback
        traceback.print_exc()

if __name__ == "__main__":
    main()
