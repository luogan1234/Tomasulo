# Tomasulo 调度算法报告 #
罗干 2014011342
高童 2014011357
王笑晗 2013011327

## 项目构成 ##
整个文件夹即Tomasulo算法的Java工程：在src目录下，包含界面文件TomasuloUI.fxml，和程序代码文件夹tomasulo，其中包含输入解析、tomasulo算法、界面控制相关的一系列java代码；example目录下包含几个测试用例，在运行程序后可以直接导入；img目录下包含界面上的一些图标图片。

## 输入解析 ##

主要实现在./src/Analyzer.java下，对TomasuloCore类进行了进一步封装，实现了对字符串输入的指令的解析。能够解析ADDD/SUBD/MULD/DIVD/LD/ST/SET指令，并对操作数的格式进行一定的判断。（在要求的指令集上加入了SET指令，主要便于从文件中导入数据）

指令格式规定如下：

| 指令            | 说明                        |
| ------------- | ------------------------- |
| ADDD Fa Fb Fc | a,b,c为[0,10]的数字           |
| SUBD Fa Fb Fc | 同上                        |
| MULD Fa Fb Fc | 同上                        |
| DIVD Fa Fb Fc | 同上                        |
| LD Fa b Rc    | ADDR=R[c]+b，a,c为[0,10]的数字 |
| ST Fa b Rc    | 同上                        |
| SET F a b     | F[a]=b，a为[0,10]的数字        |
| SET M a b     | M[a]=b，a为[0,4096]的数字      |
| SET R a b     | R[a]=b，a为[0,10]的数字        |

## 算法实现 ##

### 数据存储 ###

根据tomasulo算法要求，需要模拟浮点操作队列、缓冲区、浮点寄存器、整数寄存器、内存等部件，因此首先建立资源存储类Resource、整数寄存器类Register、浮点寄存器类FloatRegister、内存类Memory和缓冲区类Buffer，它们负责数据的存储与处理。Resource类作为全体部件的集合，其中包括11个整数寄存器、11个浮点寄存器、可存放4096个浮点数的内存，还有3个load buffer、3个store buffer、3个add buffer、2个mult buffer。

这里不同的buffer在功能上大体相同，通过type成员变量进行区分，可以重用大部分代码。

另外对指令建立Instruction类，用于存储单条指令信息，包括指令类型、操作数、指令状态等。

为了图形界面显示规定的内容，每个类都增加了一些必要的成员变量和函数。

### 执行流程 ###

建立TomasuloCore类，进行算法执行的管理任务，包括单步执行、资源初始化等，对于单步执行，可细分成指令写回、发射指令、检查缓冲区、判断是否完成四步。

指令写回：检查每个缓冲区内的指令，如果正好指令执行完所需要的时间，就计算指令结果，写入所有需要该次指令结果的浮点寄存器/内存/缓冲区操作数处，该缓冲区重新可用。

发射指令：检查未发射的最早的指令，判断其需要的缓冲区是否可用，可用就发射它进入缓冲区。

检查缓冲区：检查每个缓冲区的指令，如果正在执行，则剩余时间减1，同时检查等待中的指令，选择可以执行的指令中最早的那条开始执行。

判断是否完成：检查所有指令，如果都执行完毕则完成算法。

### 执行细节 ###

Buffer类的处理：根据type成员变量可以区分当前的Buffer类是什么buffer，其中add buffer和mult buffer，它们各自可以接受两种指令（ADD/SUB和MULT/DIV），在指令进入buffer等待执行时，需要计算Qj(除LD指令)、Qk(除LD、ST指令)的值，直接存储需要等待的buffer可以降低处理难度，值通过对应的浮点寄存器当时存储的buffer确定。

ADD与SUB指令：指令周期2，浮点加法器为2段流水线，故每次直接选择一条在add buffer中可以执行的指令开始执行即可。

LD和ST指令：各自与mem通过总线连接，故一步操作可以同时开始一条LD和一条ST指令；由于LD和ST会访问同一内存位置，且该实验整型寄存器不会修改，可在发射时就确定访存地址，故LD和ST指令开始执行需要额外判断在load &store buffer中是该内存位置等待的最早的指令。

MULT和DIV指令：浮点乘除法器流水线包括IF、ID、EX、MEM、WB，除了EX段其余4段均花费1个周期，对于乘法EX段花费6个周期，对于除法EX段花费6*6=36个周期，即EX段划分为6个小段。因为mult buffer只有2个，即同时执行的MULT和DIV至多2条，和其他缓冲区指令一样一次只能选一条开始。判断一条指令能否执行，需要考虑已经有一条指令在执行的情况，若正在执行的是MULT指令，那么可以直接开始不会冲突；若正在执行的是DIV指令，我选择保守的处理方法，等DIV执行36个周期再允许指令开始，这样当DIV完成EX段时新指令正好进入EX段，如果考虑DIV在EX段的小段位置来避免冲突，的确可以再减少指令总体执行时间，但这样增加了取模运算，不利于硬件实现。

## 图形界面 ##

图形界面整体在 JavaFX 框架下实现。图形界面的代码文件主要包括：

- Main extends Application 类，作为整个工程主类和图形界面的入口类，也包括界面显示的所有数据；
- UIController 类，包括所有UI相关的控件和控制函数；
- 各种内核和UI显示的接口类，包括 BufferTableItem, FloatRegisterTableContent, InstructionTableItem, MemoryTableItem 和 RegisterTableContent；
- FXML格式的UI设计文件。

### UI设计 ###

UI中的元素包括以下内容：

- 6个按钮：输入单条指令、从文件读入多条指令、单步执行、N步执行、重新开始、设置N的值。
- 8个表格：指令队列、运行状态、Load Queue、Store Queue、运行状态、内存值、浮点寄存器值、整数寄存器值。
- 显示当前执行步数的标签。

其中，“输入单条指令”、“设置N的值”两个按钮，在按下时会弹出文本输入对话框，提示用户输入指令或N的值。按下“从文件读入多条指令”按钮后，会弹出文件选择器，让用户选择仅限文本格式（txt）的指令列表。如果指令格式错误，会弹出错误信息框。

内存、寄存器的三个表格中，有关数据的部分可以直接编辑，回车提交修改，实现UI输入数据。

所有的表格都不可重新排序，以实现数据有序排序。

### 程序状态对UI的影响 ###

为了让UI在程序执行的不同情况下有不同表现，对程序执行的过程设置了三个状态：**开始前、执行中、结束后**。

开始前：当没有一条指令时，使能**输入单条指令、从文件读入多条指令、重新开始、设置N的值**四个按钮，禁用**单步执行、N步执行**两个按钮，可以编辑内存、寄存器表格。当至少输入一条指令后，启用**单步执行**按钮，点击即进入“执行中”状态。

执行中：使能**单步执行、N步执行、重新开始、设置N的值**四个按钮，禁用**输入单条指令、从文件读入多条指令**两个按钮，不可编辑内存、寄存器表格。开始执行后，指令不可新加，内存、寄存器只能由程序修改。此时可通过单步、N步执行运行语句，直到运行结束。

结束后：使能**重新开始、设置N的值**四个按钮，禁用**单步执行、N步执行、输入单条指令、从文件读入多条指令**四个按钮，不可编辑内存、寄存器表格。此时可点击“重新开始”继续新一轮模拟。

### Property类型接口 ###

为了适应JavaFX框架，在内核与UI之间设计了若干由Property机制设计的接口类。Property类的特点是可以增加监听器，在内容发生变化时，自动通知UI更新，使UI和内容保持同步。由于内核未采用Property机制，因此新增接口类，接口类和内核保持同步，UI中的表格都从接口类中获取数据。