# Image-Processor
Layered Image Processing Application using Java Swing GUI library. Supports .ppm, .jpg, .jpeg, and .png file formats.

=================Overall Project Structure & Image Usage=================
We organized our project to reflect the responsibilities of each MVC component. The packages
model.image and model.operations exist inside the model package as the models should handle how
IImages and IOperations interact to represent image processing. model.operations.colortransform and
model.operations.filter represent the appropriate organization for the currently supported types of
image operations. model.image.patterns also contains the PatternCreator interface and the 
CheckerBoardCreator class, as those are strictly related to the IImage interface, which exists 
inside model.image. Previously, our importexport package was on the same level as the model, 
but since adding the controller to our program, we felt that it would be better placed in the controller 
package, since the controller is the intermediary between the view and model. 

Additionally, all images used in the /res folder are from team members' cell phones
and all other images, including the checkerboard images and the edited versions of the original photos. 

=================Mosaic Effect ================= 
Mosaic (implements IOperation): 
We implemented the mosaic editing of an image by extending the existing operation interface. Using the MOSAIC option 
in the GUI, the user can apply the Mosaic filter to the current image. However, a design choice we made was that the 
number of seeds were a certain percentage of the image's dimensions. We made this choice because of computer 
processing differences and for the best looking-image. In the current edition, the ratio is 4%, so in a 100,000
pixel photo, the number of seeds would be 100,000 * 0.04 = 4,000 seeds. 

We included 3 versions of mosaicking. The original image is "cyrus", with the 
following percentage mosaicking (as noted by the image name):
4%
8%
12%

=================View (Text only)================= 
The view contains one interface, IImageView, which contains two methods: renderMessage and renderIImage. 
The interface is extended by BasicView, and is in charge of delivering messages to the user 
and rendering the images. The class uses an Appendable to display messages to the user. 
If the appendable fails, it will be replaced by a message in the console. 

=================View================= 
In our updated view model, we have an interface, ISwingView, that uses Java Swing and allows the user 
to interact with a window, containing the layers, their images, and the different types of filters. The 
ISwingView interface contains several methods: setListener(ActionListener), startViewing(),  
adjustLayeredState(int, int), and openDialogBox(DialogType, String...). The first method, setListener, sets 
all the view's buttons, dropdowns, etc. to the ActionListener provided in the parameter. The second method, 
startVieweing(), renders the GUI visible. It was a design choice that we made to create a separate method 
for the visibility of the window, because we wanted the controller to dictate the visibility. The fourth method, 
adjustVisibilityState(List<Integer>, int), adjusts the visiblity checkbox and allows the user to adjust 
whether the layer should be visible or not. The fifth method, adjustLayeredState(int, int, Map<Integer, String>), 
takes in the number of layers, the index of the current layer, and the names of the indicies in order to render
the components of the view. The method will throw an IAE if the numbers are out of bounds. The method does not 
enforce the representation of the model (the controller does). Lastly, openDialogBox(DialogType d, String...) 
takes in the object to be displayed in the JFrame and returns the file chosen by the user. 

All three methods return void, except for openDialoug, which returns the designated file. 

The view also includes the jar's main, FinalCmdLineEntryPoint. 

=================Controller================= 
For our controller, we have two interfaces, IController and IImageCommand. The user interacts with 
the IController, which uses the IImageCommand interface to execute the commands. The user can input 
the commands through the console, or import a batch of commands through the "batch" command. 

The IController interface handles user interactions with the ImageController class. 
The ImageController class then delegates to the appropriate class implementing the IImageCommand. 
The ImageController class also renders messages to the user, confirming command success or 
delivering an error message. 

The IImageCommand interface contains one method: go. The interface is implemented by: 
- AddBlank (creates a blank layer) 
- ClearAll (clears all the layers)
- CurrentLayer (returns the index of the current layer)
- NumLayers (returns the number of layers)
- SetCurrent (sets a designated layer to the current layer)
- MakeVis (makes the current layer visible)
- MakeInvis (makes the current layer invisible) 
- Remove (removes the current layer)
- Export (exports the given image)
- Import (imports from the file path)
- Batch (allows the user to process a batch of commands) 
- Blur (blurs the current layer's image)
- Sepia (sepias the current layer's image)
- GreyScale (greyscales the current layer's image)

The user can only manipulate one layer at a time. To traverse the layers, the user must set the 
current layer to the index they desire. 

Most Recent Edits:
1) Created a new controller class, SwingController. It is designed to fill in the blanks between
using the console-only input method and the Swing view. SwingController implements the 
IController (mentioned above) and ActionListener (built-in) interfaces. SwingController contains the methods
startProcessing(), which sets the listeners for the view; getTopMostVisibleLayer(), which returns the top-most
visible layer to be displayed. 
2) Created a private class for each listener object. 
- ButtonsListener handles button interactions 
- VisibleCheckBoxListener handles the visibility checkbox on the window
- OpsDropdownListener handles the dropdown menus for current operation 
- LayerSelectListener handles the dropdown menu for the layers dropdown.
- IOListener handles the importing and exporting of images and files
- LayerRenameListener handles the renaming of the separate layers. 

=================Commands*================= 
add blank: adds a blank layer
num layers: returns the number of layers
current layer: returns the number of the current layer
set current: sets the current layer to the specified, given number
remove: removes the current layer
make vis: makes a layer visible (layers are created visible)
make invis: makes a layer invisible 
sharpen: applies the sharpen filter to the current layer
blur: applies the blur filter to the current layer
greyscale: applies the greyscake filter to the current layer
sepia: applies the sepia filter to the current layer
batch: imports a batch of commands from the file source

*also see USEME 

=================Features================= 
- Importing an image to the model
- Altering an image's color, using Greyscale and Sepia classes
- Altering an image's sharpness, using BlurFilter and SharpenFilter classes
- Exporting the altered image to the file path as designated 
- Creates a checkerboard image with the given dimensions, using CheckerBoardCreator class
- Creates layered representation for images, along with a text-based input controller
- Allows the filters to be applied to the current layer
- Allows the user to set a current layer using the index and manipulate the current layer
- Allows users to check what layer they're currently on
- Allows users to clear all the layers
- Allows the user to remove the current layer
- Exporting and importing an image to/from a designated layer to/from a specified file path
- Creates a view window using Java Swing. Users can interact with the window to change their layers.


=================Layering Capabilities (changes to model for HW06)=================
We added an additional interface and class to the model, called ILayeredModel and LayeredProcessorModel, 
respectively. The interface (and class) handles multiple layered images. The class LayeredProcessorModel 
extends BasicProcessorModel, since most of the methods can be abstracted. However, it does have 
unique methods to handle multi-layered images. A summary:
- makeLayerInvisible(int) -- takes in an index of the layer and makes it invisible
- makeLayerVisible(int) -- takes in an index of the layer and makes it visible
- isLayerInvisible(int) -- takes in an index of the layer and returns true if the layer is invisible, 
	false if not
- getCurrentLayer() -- observer method to examine which layer is currently being modified 
- setCurrentLayer(int) -- takes in an index of the layer and sets the given layer to current 
- addBlankLayer() -- adds a blank layer to the top level that matches the size of the image already loaded. 
	If no images are loaded, the default dimensions are 100x100 pixels
- addImage(IImage) -- adds an IImage to the current layer. Throws an IAE if the image is not the same size as the 
	other layers.
- removeAt(int) -- removes the layer at the given index
- applyIOperation(OperationType, int) -- performs the given OperationType on the image at the given index
- addFromPattern(PatternCreator) -- adds a self-generated image to the layer.
- getInvisibleLayers() -- an observer method that returns a copy of the invisible layers as an ArrayList<Integers>

Of all the methods above, those with integer parameters that specify indicies of layers will throw an 
IllegalArgumentException if the layer does not exist or is out of bounds.

Additionally, we created an adapter to enhance our code. The interface IOperationAdapter is implemented by the
IOperationAdapterImpl class, containing one method, adaptOperation(OperationType, IImage), which takes in the 
enum representation of the IOperation to perform and the IImage to be applied to. The method throws an IAE if 
either argument in the method is null. The purpose of the interface and class is to simplify the communiation
between the controller and the model. 

In the model, we made a few changes. The first major change is the addition of the Mosaic class, in order to 
render an image with the Mosaic filter (extra credit). The constructor for Mosaic takes in an IImage to be edited, 
the int number of seeds, and a Random. The class contains the method apply(), which applies the mosaicking to the 
given Mosaic. The class also contains overrides for hashCode and equals methods. 

=================Importing / Exporting Capabilities (changes to import/export for HW07)=================
We moved our importexport package from the model to the controller. In the package, we have 
two interfaces, IImporter and IFileExporter. The IImporter contains one method, importFrom, as before, 
that takes in a String file name. IImporter is implemented by two classes, 
BasicPpmImporter and AdvancedBasicImporter. BasicPpmImporter handles, as before, .ppm images. 
AdvancedBasicImporter handles importing other 
file types, such as .png or .jpg. The method in both classes will throw IllegalArgumentExceptions 
if the file is not found, null, or if the file doesn't fit the specifications of the file type 
the implementation is meant to handle. 

The exporting aspect contains one interface, IFileExporter, which is implemented by AdvancedUtilExporter, contains 
one method: export, which takes in an IImage and a String for a file name. The method throws an IAE if either
of the given parameters is null, and throws an IOException if the file writing fails. 


In HW07, when exporting, the file extension desired must be added to the end of the image (ie: ...\someName.png). 
This was a design choice for the user to specify the kind of extension that the image was saved by. Additionally,
when exporting all the layers at once, the selected name is applied to the .txt file, and the rest of the 
images are saved with the layer name. 

Lastly, we created a FileUtil class that handles the importing and exporting of files and images to their 
desired formats. The class contains methods that are relatively self-explanatory and parses the given filename to 
get the desired file type. 
=================Image/Pixel Representation=================

In our model, we have two interfaces, IImage and IPixel, that represent a single image and pixel,
respectively. 

IImage is implemented by BasicImage, a simple representation of an image that holds 
a private final field of a 2D-array of IPixel, and is constructed with two integers representing 
width and height, and an array of IPixel to populate the image. The constructor will throw an 
IllegalArgumentException if either the height or width is less than or equal to zero, or if the 
length of the array of IPixels is not equal to the product of both height and width (as that should
represent the total number of pixels). The BasicImage constructor also throws an 
IllegalArgumentException if any pixel in the given array has a position out of the bounds of the 
width and height of the image, and if there are any repetitions in position. Thus, the BasicImage
constructor enforces a class invariant that all IPixels in the 2D IPixel field fully populate all
array elements, and that each IPixel in this array represents a unique position in the image. The
BasicImage class implements four public methods from the IImage interface, the first two being 
observer methods, getHeight() and getWidth(), that return the height and width of the image. 
Another method, getPixelAt(), which takes in an int x and y, returns the IPixel at the specified 
location. The method will throw an IllegalArgumentException if either parameter is less than zero
or greater than the image dimensions.  Lastly, the interface contains a factory method, 
createImage(), that returns a new IImage of the same type of object that implemented it (like 
BasicImage), to help decouple classes that may create new IImages, like IOperation implementations,
from any specific implementation of IImage. 

A BasicPixel, which implements the IPixel class, is constructed with five integer arguments: the x
and y coordinate of the pixel, and the RGB channel values that represent the color of the
BasicPixel. IllegalArgumentExceptions will be thrown if either the x or y value of the pixel’s
position is less than zero. In the BasicPixel constructor, the RGB values are clamped to fit the
range [0,255]. Any value less than zero is overwritten as zero and any value greater than 255 is
overwritten as 255. The BasicPixel class also contains five observer methods for each of the 
fields. The class also contains a factory method that returns a new IPixel, of the type on the 
implementation from which the method was called, that takes in doubles instead of integers for RGB 
values, as we feel that it is the responsibility of each IPixel implementation to determine how to 
convert from double values to integer values, which in the case of BasicPixel is simply to floor. 
As with the factory method in IImage, this is to decouple other classes from any specific 
implementation of IPixel. Lastly, the class overrides the equals() and hashcode() methods by 
comparing the fields of two BasicPixels and hashing with a combination of the BasicPixel's fields 
with the intention of simplifying testing. Note, while BasicPixel enforces an invariant that x and
y are at least 0, and RGB values are [0, 255], it doesn't enforce that its x and y values are 
within the bounds of the IImage dimensions it exists within, as we have each implementation of 
IImage enforce the property that IImages cannot be constructed from pixels outside of their 
width/height as that is a responsibility of images and not pixels. 

=================Image Operations=================
In order for our model to handle processes to apply to images, we have an interface IOperation that
has a single method apply() that returns an IImage that represents the result of applying whatever 
implementation of IOperation is applied. We let each implementation decide how to take in an image,
but for now we have had all implementations be a function object that is constructed with a 
specific IImage to be apply an effect to. We have one abstract class that implements IOperation, 
and that is AbstractOperation, which is constructed with a single IImage and implements apply() 
in a way that iterates through all IPixels in the IImage it was constructed with, and applies a 
protected abstract method pixelApply() to each IPixel to generate a new IPixel to create a new 
IImage to return. The implementation of pixelApply() is up to the subclasses of AbstractOperation,
which currently are AbstractFilter and AbstractColorTransform. AbstractOperation holds a single 
protected final IImage field that it is constructed with, that subclasses will use to perform their
computations. 

For the color transformations, greyscale and sepia, the AbstractColorTransform class extends the 
AbstractOperation class. The AbstractColorTransform class contains a final protected field of 
colorMatrix, which is a square, 2D array of doubles of length 3, as each linear color 
transformation requires this. Just like the parent class, the constructor takes in an IImage, and 
will throw an IllegalArgumentException if the image is null due to calling super with that IImage. 
This class implements the method pixelApply, which takes in an x and y value representing a given 
IPixel within the IImage field inherited from AbstractOperation and calculates the new RGB values 
for the transformation and returns an IPixel. This abstract color class is extended by Greyscale 
and Sepia, and leaves adding more transformations very simple as one only needs to extend 
AbstractColorTransform to add a new transformation. Both classes’ constructors will throw 
IllegalArgumentExceptions if the given IImage is null, as enforced by the constructor for 
AbstractOperation, and each initialize colorMatrix to the matrix values required to apply their 
effects. These subclasses cannot change the size of colorMatrix, as they should, as the field is 
final as specified in AbstractColorTransform. 

Similar to the AbstractColorTransform class, AbstractFilter also extends the AbstractOperation
class. The constructor takes in an IImage, which will throw an IllegalArgumentException if the 
image is null, as enforced by the AbstractOperation constructor. AbstractFilter has a protected 2D
matrix of doubles kernel field for filter subclasses to use, that is not final as each filter may 
need a different kernel size, and thus initialization of this field is left up to the subclasses. 
AbstractFilter's constructor cannot enforce a specific size for the kernel, nor can it enforce the 
property of having odd length and width and square kernels, so the class's implementation of 
pixelApply requires that such properties are true and throws an IllegalStateException otherwise, in 
order to have the class enforce that invariant in some way. The method pixelApply takes in two 
integers, an x and y value that represents the location of the pixel within the IImage field 
inherited from AbstractOperation, and applies the math and produces a new IPixel. This abstract 
filter class is extended by BlurFilter and SharpenFilter, whose constructors initialize the kernel
to a 3x3 and 5x5 size, respectively, and populate the kernel with the values per their 
specifications. Per the design principle of "closed to modification and open to extension", 
AbstractFilter can perform the applyPixel operation regardless of what kernel size is possessed by 
its subclass as long as kernels have odd and equal length and width. Therefore, adding a new 
filter, even with a different kernel size, can be done simply by extending AbstractFilter and 
initializing kernel to the required dimensions and shape, and any new filter subclasses that 
violate this principle will cause an IllegalStateException if they attempt to use their apply() 
method. Therefore, AbstractFilter allows the process of adding new filters to be smooth and ensures
 that nothing needs to be modified to make these hypothetical new filters to work.  

=================PatternCreator Interface================= 

The PatternCreator interface, which is implemented by CheckerBoardCreator class, helps us have an
easy way to create IImages programmatically, for purpose such as having our IProcessorModel deal 
with IImages that did not originate from client-based programmtically generated IImages or strictly
imported IImages. PatternCreator only has one method .create() that returns the IImage created by 
the PatternCreator implementation's own rules. 

CheckerBoardCreator's purpose is to create an IImage with a checkerboard pattern with specified
dimensions. The constructor takes in three integers: width, height, and the sizes of the different
colored cells, throwing an IllegalArgumentException if any of the parameters are less than or equal
to 0. As according to the interface, this class contains one method, create(), that uses the 
dimensions and creates a checkerboard pattern.

=================Model Interface and BasicProcessorModel=================
Finally, our model contains the IProcessorModel interface, which is implemented by the
BasicProcessorModel class. The interface contains eight methods, addImage(IImage), getImageAt(int), 
removeAt(int), numImages(), applyIOperation(IOperation), importFrom(String, IImporter), 
exportTo(IFileExporter, int, String), and addFromPattern(PatternCreator). 

We chose to interpret the model interface as a way to store a "history" of IImages processed or 
stored in the model, and the methods as ways to interact or remove IImages from this history. 
BasicProcessorModel represents this history of IImages as an ArrayList of IImages initialized as an
empty ArrayList in the constructor. The design choice to rely on function objects in the parameters
of methods (IOperation, IImporter, IFileExporter, and PatternCreator) is to only couple our model 
to the IImage interface, which those interfaces are also coupled to. This design choice allows us 
to easily use new version of those interfaces to handle other file types or classes. For example, 
if we needed to create a JPEG exporter we could simply create a new IFileExporter implementation
and pass that function object to the model. While we would still need to make changes somewhere, 
we would not have to change anything about our model to handle those operations. This use of IImage
in BasicProcessorModel also ensures that we can have multiple types of IImage objects to interact 
within a single model. 

The methods getImageAt and removeAt both return an IImage according to the given index, and 
numImages returns an integer representing the number of IImages in the history of the model, 
specifically the size of the ArrayList<IImage> field when implemented in BasicProcessorModel. The
rest of the methods return void, but have specific side effects such as adding a new IImage to the 
history of processed IImages or exporting to a specific directory. All methods that take an 
integer, such as removeAt, getImageAt, and exportTo apply their effects to the place in the history
of IImages represented by that integer, and specifically in the BasicProcessorModel implementation 
the indexed place based on the int value. Each of those methods that deal with this int-based 
indexing also throw an IllegalArgumentException if the given int is less than 0 or greater than the
number of IImages in the model - 1. Every method that takes in a function object or IImage 
parameter, such as addImage, applyIOperation, importFrom, exportTo, and addFromPattern also throw 
an IllegalArgumentException if given null. As for exceptions that result from other reasons, 
importFrom throws an IllegalArgumentException if the file is not found, as it calls 
IImporter.importFrom which does exactly that. exportTo throws an IllegalStateException if 
IFileExporter.export throws an IOException, which can happen if file writing fails. 

getImageAt, addImage, removeAt, and numImages are methods that help a controller or view mutate and
observe the state of the model, specifically interact and remove IImages from the history of 
IImage objects processed. getImageAt and removeAt, as specified before, both return a reference
to the IImage at the given integer index, while getImageAt makes no changes to the IImages in the
model while removeAt does remove the specified IImage from the model. addImage adds a defensive
copy of the given IImage to the model, while numImages returns the total number of IImages in the
model.

The applyIOperation method takes in an IOperation and adds to the history of images a new image 
based on the IImage the IOperation was constructed with, which could be an IImage originating from
IProcessorModel, as a new IOperation could be created with an IImage from IProcessorModel by using
getImageAt or removeAt. Whether the user wants to replace an image when applying an effect to such
an image is up to the controller and application this model is used with and not the model's 
responsibilty, and we did not want to restrict future extensions of this model by enforcing one way
or another. 

The addFromPattern method takes in a PatternCreator and returns void. In the BasicProcessorModel
implementation this method adds the IImage created from the PatternCreator.create() method to the
ArrayList<IImage> field. importFrom takes in a String and and IImporter object, and uses the 
importFrom method in IImporter to convert the file with the name and directory specified by the 
String argument corresponding to the type of IImporter object used to an IImage type. The resulting
IImage is then added to the history of IImages processed. exportTo takes in a IFileExporter, an 
int, and a String, and it uses the IFileExport object which is compatible with a specific type of 
file to export the IImage at the index represented by the integer to a file of the name and 
directory of the given String, and does not remove the specified IImage from the history of IImages
processed and does not add a new IImage to the history of IImages processed, at least for 
BasicProcessorModel.


Note, getImageAt, addImage, removeAt, applyIOperation, addFromPattern, importFrom, and exportTo all
interact with IImage references, and so each time an IImage is returned or passed to a funciton 
object, BasicProcessor model uses defensiveCopyGenerator is used to ensure that IImages inside the
model cannot be mutated from outside the model. defensiveCopyGenerator and pixelsFromImage are both
private methods unique to BasicProcessorModel, meant to make defensive copies of any IImage 
composed of copies of their individual pixels that is received or returned by any public-facing 
methods, in the event there are any interface implementations or classes that use the model that 
have the ability to mutate IImages. While we are aware that these private methods rely on the 
.createImage/.createPixel methods to create these copies, which are meant to decouple the model 
from the BasicImage/BasicPixel class, and new implementations of IImage/IPixel could change those
methods in a way that could allow outside mutation, we feel like the benefit of decoupling is worth
more than the risk of having IImage/IPixel having such implementations. Additionally, the risk of
having new implementations of function objects that mutate IImage objects while handling IImages is
much higher than the risk having new implementations of IImages that possess that ability to mutate,
especially given that we are told to anticipate handling more file types, which at least tells us to
have more IFileExporter/IImporter implementations. Therefore, we choose to have these private 
defensive copy methods use IImage/IPixel factory methods. 

Currently, BasicProcessorModel is the only implementation of IProcessorModel, but in anticipation
to new models we have included a builder pattern class ProcessorModelCreator, that contains an
integrated enum ProcessorType that has two values BASIC and NONBASIC. Currently, NONBASIC doesn't
exist, but it is a placeholder value for future implementations of IProcessorModel. 
ProcessorModelCreator has a create(ProcessorType) method that returns a new IProcessorModel
according to the given enum value, and currently returns null if given NONBASIC. 
