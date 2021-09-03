=================To Run==================
In GUI mode: double click LIME.jar. 

From command prompt:
  C:\User\...\> java -jar LIME.jar -script path-of-script
  ---To run only as a script (.txt, .bat file extensions, see commands below) and exit after. Will not open GUI.
  
  C:\User\...\> java -jar LIME.jar -text
  ---To run in command prompt. 
  
  C:\User\...\> java -jar LIME.jar -interactive
  ---Run in GUI mode. 


=================GUI Control======================

Use "To Apply: " dropdown menu to select desired effect to be applied to the image, and press "Apply" to 
use on the current image. 

Use Run Script, Import, Export, and Export All to perform the respective actions through the file explorer
window. 

Enable and disable layer visibility alongside the other Layer Manipulation controls, the currently displayed
layer will be the highest visible layer below or at the currently selected layer. The "bottom" image is
at the top of the "Current Layer" dropdown list, and the "top" layer is at the bottom of the menu.

Use the "Add Blank Layer", "Add Copy", and "Remove Layer" to perform additional actions. Note, all layers must
be the same size as the current layer, and new blank layers will be sized to fit whatever layer they appear 
above. Remove all layers to reset the acceptable size of layers. 

See the interaction text bar at the bottom of the application window to see program output, including errors
and responses to actions.  

=================Commands================= 
add blank		adds a blank layer
num layers		returns the number of layers
current layer		returns the number of the current layer
set current [index]	sets the current layer to the specified, given number
remove			removes the current layer
make vis		makes a layer visible (layers are created visible)
make invis		makes a layer invisible 
sharpen			applies the sharpen filter to the current layer
blur			applies the blur filter to the current layer
greyscale		applies the greyscake filter to the current layer
sepia			applies the sepia filter to the current layer
import [file name]	imports the given file name to the current layer
export [file name]	exports the image from the current layer to the given file path
batch  [file path]	imports a set of commands from a txt and executes them



**For "set current", put a space between the command and the index (ie: "set current 2")
**Proper spelling (please)
**The first of a comment must start with a pound key from the end of a command (ie: "blur #pizza)
**If the entire line is a comment, then the entire comment must be on the left-most column 
**No commands should have trailing spaces. 
**Be sure the specify the file directory in the file name 


**Export all in the GUI exports all the imagees in the session, regardless of whether or not they are 
invisible or not. The txt file will, however, make a note of which ones are invisible. 
**Most of the buttons and dropdowns in the GUI are self-explanatory. Please remember to hit "apply" 
after selecting a desired filter. 

**The jar file can be run using double-click. 




