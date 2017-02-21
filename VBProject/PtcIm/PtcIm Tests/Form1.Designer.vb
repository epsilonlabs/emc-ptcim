<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()>
Partial Class Form1
    Inherits System.Windows.Forms.Form

    'Form overrides dispose to clean up the component list.
    <System.Diagnostics.DebuggerNonUserCode()>
    Protected Overrides Sub Dispose(ByVal disposing As Boolean)
        Try
            If disposing AndAlso components IsNot Nothing Then
                components.Dispose()
            End If
        Finally
            MyBase.Dispose(disposing)
        End Try
    End Sub

    'Required by the Windows Form Designer
    Private components As System.ComponentModel.IContainer

    'NOTE: The following procedure is required by the Windows Form Designer
    'It can be modified using the Windows Form Designer.  
    'Do not modify it using the code editor.
    <System.Diagnostics.DebuggerStepThrough()>
    Private Sub InitializeComponent()
        Me.ExecBtn = New System.Windows.Forms.Button()
        Me.OutTextBox = New System.Windows.Forms.RichTextBox()
        Me.ModelListView = New System.Windows.Forms.ListView()
        Me.AddModelBtn = New System.Windows.Forms.Button()
        Me.RemoveModelBtn = New System.Windows.Forms.Button()
        Me.Label1 = New System.Windows.Forms.Label()
        Me.Label2 = New System.Windows.Forms.Label()
        Me.Label3 = New System.Windows.Forms.Label()
        Me.numOfIterations = New System.Windows.Forms.NumericUpDown()
        Me.browseBtn = New System.Windows.Forms.Button()
        Me.pathTxt = New System.Windows.Forms.TextBox()
        Me.vbCheck = New System.Windows.Forms.CheckBox()
        Me.epsiloNoCheck = New System.Windows.Forms.CheckBox()
        Me.epsilonAttrCheck = New System.Windows.Forms.CheckBox()
        Me.epsilonValuesCheck = New System.Windows.Forms.CheckBox()
        Me.epsilonFullCheck = New System.Windows.Forms.CheckBox()
        CType(Me.numOfIterations, System.ComponentModel.ISupportInitialize).BeginInit()
        Me.SuspendLayout()
        '
        'ExecBtn
        '
        Me.ExecBtn.Enabled = False
        Me.ExecBtn.Location = New System.Drawing.Point(222, 275)
        Me.ExecBtn.Name = "ExecBtn"
        Me.ExecBtn.Size = New System.Drawing.Size(122, 41)
        Me.ExecBtn.TabIndex = 1
        Me.ExecBtn.Text = "Run Tests"
        Me.ExecBtn.UseVisualStyleBackColor = True
        '
        'OutTextBox
        '
        Me.OutTextBox.Location = New System.Drawing.Point(21, 335)
        Me.OutTextBox.Name = "OutTextBox"
        Me.OutTextBox.Size = New System.Drawing.Size(323, 329)
        Me.OutTextBox.TabIndex = 2
        Me.OutTextBox.Text = ""
        Me.OutTextBox.UseWaitCursor = True
        Me.OutTextBox.WordWrap = False
        '
        'ModelListView
        '
        Me.ModelListView.Location = New System.Drawing.Point(21, 42)
        Me.ModelListView.Name = "ModelListView"
        Me.ModelListView.Size = New System.Drawing.Size(268, 110)
        Me.ModelListView.TabIndex = 3
        Me.ModelListView.UseCompatibleStateImageBehavior = False
        Me.ModelListView.View = System.Windows.Forms.View.List
        '
        'AddModelBtn
        '
        Me.AddModelBtn.Font = New System.Drawing.Font("Microsoft Sans Serif", 15.75!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.AddModelBtn.Location = New System.Drawing.Point(295, 42)
        Me.AddModelBtn.Name = "AddModelBtn"
        Me.AddModelBtn.Size = New System.Drawing.Size(49, 47)
        Me.AddModelBtn.TabIndex = 4
        Me.AddModelBtn.Text = "+"
        Me.AddModelBtn.UseVisualStyleBackColor = True
        '
        'RemoveModelBtn
        '
        Me.RemoveModelBtn.Font = New System.Drawing.Font("Microsoft Sans Serif", 15.75!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.RemoveModelBtn.Location = New System.Drawing.Point(295, 105)
        Me.RemoveModelBtn.Name = "RemoveModelBtn"
        Me.RemoveModelBtn.Size = New System.Drawing.Size(49, 47)
        Me.RemoveModelBtn.TabIndex = 5
        Me.RemoveModelBtn.Text = "-"
        Me.RemoveModelBtn.UseVisualStyleBackColor = True
        '
        'Label1
        '
        Me.Label1.AutoSize = True
        Me.Label1.Font = New System.Drawing.Font("Microsoft Sans Serif", 15.75!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label1.Location = New System.Drawing.Point(16, 14)
        Me.Label1.Name = "Label1"
        Me.Label1.Size = New System.Drawing.Size(82, 25)
        Me.Label1.TabIndex = 6
        Me.Label1.Text = "Models"
        '
        'Label2
        '
        Me.Label2.AutoSize = True
        Me.Label2.Font = New System.Drawing.Font("Microsoft Sans Serif", 15.75!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label2.Location = New System.Drawing.Point(22, 307)
        Me.Label2.Name = "Label2"
        Me.Label2.Size = New System.Drawing.Size(76, 25)
        Me.Label2.TabIndex = 7
        Me.Label2.Text = "Output"
        '
        'Label3
        '
        Me.Label3.AutoSize = True
        Me.Label3.Font = New System.Drawing.Font("Microsoft Sans Serif", 12.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label3.Location = New System.Drawing.Point(23, 284)
        Me.Label3.Name = "Label3"
        Me.Label3.Size = New System.Drawing.Size(158, 20)
        Me.Label3.TabIndex = 8
        Me.Label3.Text = "Number of Iterations:"
        '
        'numOfIterations
        '
        Me.numOfIterations.Font = New System.Drawing.Font("Microsoft Sans Serif", 9.75!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.numOfIterations.Location = New System.Drawing.Point(178, 285)
        Me.numOfIterations.Maximum = New Decimal(New Integer() {10, 0, 0, 0})
        Me.numOfIterations.Minimum = New Decimal(New Integer() {1, 0, 0, 0})
        Me.numOfIterations.Name = "numOfIterations"
        Me.numOfIterations.Size = New System.Drawing.Size(34, 22)
        Me.numOfIterations.TabIndex = 9
        Me.numOfIterations.Value = New Decimal(New Integer() {3, 0, 0, 0})
        '
        'browseBtn
        '
        Me.browseBtn.Location = New System.Drawing.Point(222, 196)
        Me.browseBtn.Name = "browseBtn"
        Me.browseBtn.Size = New System.Drawing.Size(122, 23)
        Me.browseBtn.TabIndex = 10
        Me.browseBtn.Text = "Browse"
        Me.browseBtn.UseVisualStyleBackColor = True
        '
        'pathTxt
        '
        Me.pathTxt.Font = New System.Drawing.Font("Microsoft Sans Serif", 9.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.pathTxt.Location = New System.Drawing.Point(21, 169)
        Me.pathTxt.Name = "pathTxt"
        Me.pathTxt.Size = New System.Drawing.Size(323, 21)
        Me.pathTxt.TabIndex = 11
        Me.pathTxt.Text = System.IO.Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().CodeBase).Remove(0, 6)
        '
        'vbCheck
        '
        Me.vbCheck.AutoSize = True
        Me.vbCheck.Location = New System.Drawing.Point(28, 229)
        Me.vbCheck.Name = "vbCheck"
        Me.vbCheck.Size = New System.Drawing.Size(40, 17)
        Me.vbCheck.TabIndex = 12
        Me.vbCheck.Text = "VB"
        Me.vbCheck.UseVisualStyleBackColor = True
        '
        'epsiloNoCheck
        '
        Me.epsiloNoCheck.AutoSize = True
        Me.epsiloNoCheck.Location = New System.Drawing.Point(28, 252)
        Me.epsiloNoCheck.Name = "epsiloNoCheck"
        Me.epsiloNoCheck.Size = New System.Drawing.Size(111, 17)
        Me.epsiloNoCheck.TabIndex = 13
        Me.epsiloNoCheck.Text = "Epsilon No Cache"
        Me.epsiloNoCheck.UseVisualStyleBackColor = True
        '
        'epsilonAttrCheck
        '
        Me.epsilonAttrCheck.AutoSize = True
        Me.epsilonAttrCheck.Location = New System.Drawing.Point(81, 229)
        Me.epsilonAttrCheck.Name = "epsilonAttrCheck"
        Me.epsilonAttrCheck.Size = New System.Drawing.Size(131, 17)
        Me.epsilonAttrCheck.TabIndex = 14
        Me.epsilonAttrCheck.Text = "Epsilon Attributes Only"
        Me.epsilonAttrCheck.UseVisualStyleBackColor = True
        '
        'epsilonValuesCheck
        '
        Me.epsilonValuesCheck.AutoSize = True
        Me.epsilonValuesCheck.Location = New System.Drawing.Point(226, 229)
        Me.epsilonValuesCheck.Name = "epsilonValuesCheck"
        Me.epsilonValuesCheck.Size = New System.Drawing.Size(119, 17)
        Me.epsilonValuesCheck.TabIndex = 15
        Me.epsilonValuesCheck.Text = "Epsilon Values Only"
        Me.epsilonValuesCheck.UseVisualStyleBackColor = True
        '
        'epsilonFullCheck
        '
        Me.epsilonFullCheck.AutoSize = True
        Me.epsilonFullCheck.Location = New System.Drawing.Point(145, 252)
        Me.epsilonFullCheck.Name = "epsilonFullCheck"
        Me.epsilonFullCheck.Size = New System.Drawing.Size(113, 17)
        Me.epsilonFullCheck.TabIndex = 16
        Me.epsilonFullCheck.Text = "Epsilon Full Cache"
        Me.epsilonFullCheck.UseVisualStyleBackColor = True
        '
        'Form1
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.ClientSize = New System.Drawing.Size(368, 676)
        Me.Controls.Add(Me.OutTextBox)
        Me.Controls.Add(Me.epsilonFullCheck)
        Me.Controls.Add(Me.epsilonValuesCheck)
        Me.Controls.Add(Me.epsilonAttrCheck)
        Me.Controls.Add(Me.epsiloNoCheck)
        Me.Controls.Add(Me.vbCheck)
        Me.Controls.Add(Me.pathTxt)
        Me.Controls.Add(Me.browseBtn)
        Me.Controls.Add(Me.numOfIterations)
        Me.Controls.Add(Me.Label3)
        Me.Controls.Add(Me.Label2)
        Me.Controls.Add(Me.Label1)
        Me.Controls.Add(Me.RemoveModelBtn)
        Me.Controls.Add(Me.AddModelBtn)
        Me.Controls.Add(Me.ModelListView)
        Me.Controls.Add(Me.ExecBtn)
        Me.Name = "Form1"
        Me.Text = "PTC IM Benchmarking"
        CType(Me.numOfIterations, System.ComponentModel.ISupportInitialize).EndInit()
        Me.ResumeLayout(False)
        Me.PerformLayout()

    End Sub
    Friend WithEvents ExecBtn As Button
    Friend WithEvents OutTextBox As RichTextBox
    Friend WithEvents ModelListView As ListView
    Friend WithEvents AddModelBtn As Button
    Friend WithEvents RemoveModelBtn As Button
    Friend WithEvents Label1 As Label
    Friend WithEvents Label2 As Label
    Friend WithEvents Label3 As Label
    Friend WithEvents numOfIterations As NumericUpDown
    Friend WithEvents browseBtn As Button
    Friend WithEvents pathTxt As TextBox
    Friend WithEvents vbCheck As CheckBox
    Friend WithEvents epsiloNoCheck As CheckBox
    Friend WithEvents epsilonAttrCheck As CheckBox
    Friend WithEvents epsilonValuesCheck As CheckBox
    Friend WithEvents epsilonFullCheck As CheckBox
End Class
