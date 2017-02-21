Imports System.Text.RegularExpressions
Imports System.Text
Imports System.Threading

Public Class Form1

    Dim strReference
    Dim builder As New StringBuilder
    Dim errorBuilder As New StringBuilder
    Dim errorsFile, file As System.IO.StreamWriter
    Dim id
    Dim allModelsList As New ArrayList()
    Dim projects = CreateObject("OMTE.Projects")
    Dim numberOfRepeats = 3
    Dim timeElapsedParse = 0
    Dim numberOfTotalErrors

    Private Sub AddModelBtn_Click(sender As Object, e As EventArgs) Handles AddModelBtn.Click
        Dim Obj = CreateObject("COMGUIUtil.ArtisanModelFileDialog")
        strReference = Obj.Create(True)
        allModelsList.Add(strReference)
        projects = CreateObject("OMTE.Projects")
        Dim modelName = projects.Item("Reference", strReference).Property("Name")
        Dim modelId = projects.Item("Reference", strReference).Property("Id")
        Dim nameAndIdString = modelName + " (id:" + modelId
        ModelListView.Items.Add(nameAndIdString)
        For Each modelInList As Object In allModelsList
            Dim name = projects.Item("Reference", modelInList).Property("Name").ToString
        Next
        If (ModelListView.Items.Count > 0) Then
            ExecBtn.Enabled = True
        End If
    End Sub

    Private Sub RemoveModelBtn_Click(sender As Object, e As EventArgs) Handles RemoveModelBtn.Click
        For Each item As ListViewItem In ModelListView.SelectedItems
            Dim nameAndIdString = item.Text
            Dim id = GetIdFromNameAndId(nameAndIdString)
            For Each modelInList As Object In allModelsList
                If (projects.Item("Reference", modelInList).Property("Id").Equals(id)) Then
                    allModelsList.Remove(modelInList)
                    ModelListView.Items.Remove(item)
                    Exit For
                End If
            Next
        Next
        For Each modelInList As Object In allModelsList
            Dim name = projects.Item("Reference", modelInList).Property("Name").ToString
        Next
        If (ModelListView.Items.Count = 0) Then
            ExecBtn.Enabled = False
        End If
    End Sub

    Private Sub ExecBtn_Click(sender As Object, e As EventArgs) Handles ExecBtn.Click
        numberOfRepeats = numOfIterations.Value
        Dim now = DateTime.Now
        Dim timestamp = now.Year.ToString + now.Month.ToString + now.Day.ToString + now.Hour.ToString + now.Minute.ToString + now.Second.ToString
        Dim outputFilePath = pathTxt.Text + "\results_" + timestamp + ".txt"
        file = My.Computer.FileSystem.OpenTextFileWriter(outputFilePath, False)
        Dim errorsFilePath = pathTxt.Text + "\errorReport_" + timestamp + ".txt"
        errorsFile = My.Computer.FileSystem.OpenTextFileWriter(errorsFilePath, False)
        OutTextBox.ForeColor = Color.Red
        Call OutTextBox.Clear()
        'DONT DELETE THIS IS TO SET CURRENT PATH IN FORM1.DESIGNER.VB
        ' System.IO.Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().CodeBase).Remove(0, 6)
        'WarmUpDB(projects.Item("Reference", strReference).Item("Dictionary", "Dictionary"))
        builder.Append("Id,Name,Elements,Iteration,ParsingTime,ExecutionTime,Errors,Approach").AppendLine()
        errorBuilder.Append("Approach,Message,ErrorId").AppendLine()
        If (vbCheck.Checked) Then
            OutTextBox.Text += "VB Benchmarking Started..." + vbCrLf
            OutTextBox.Refresh()
            For Each modelInList As Object In allModelsList
                For repeatExperiment As Integer = 1 To numberOfRepeats
                    numberOfTotalErrors = 0
                    Dim stopWatch As New Stopwatch()
                    Dim project = projects.Item("Reference", modelInList)
                    Dim dictionary = project.Item("Dictionary", "Dictionary")
                    OutTextBox.Text += "Model: " + project.Property("Name").ToString + "... "
                    OutTextBox.Refresh()
                    id = project.Property("Id")
                    Dim numOfElements = dictionary.ItemCount("")
                    builder.Append(id)
                    Dim modelName = project.Property("Name")
                    builder.Append("," + modelName + "," + numOfElements.ToString + "," + repeatExperiment.ToString + "," + timeElapsedParse.ToString + ",")
                    stopWatch.Start()
                    errorBuilder.Append(CheckConstraint1(dictionary))
                    errorBuilder.Append(CheckConstraint2(dictionary))
                    errorBuilder.Append(CheckConstraint3(dictionary))
                    errorBuilder.Append(CheckConstraint4(dictionary))
                    errorBuilder.Append(CheckConstraint5(dictionary))
                    errorBuilder.Append(CheckConstraint6(dictionary))
                    errorBuilder.Append(CheckConstraint7(dictionary))
                    errorBuilder.Append(CheckConstraint8a(dictionary))
                    errorBuilder.Append(CheckConstraint8b(dictionary))
                    errorBuilder.Append(CheckConstraint9a(dictionary))
                    errorBuilder.Append(CheckConstraint9b(dictionary))
                    builder.Append(stopWatch.ElapsedMilliseconds.ToString)
                    builder.Append(",")
                    builder.Append(numberOfTotalErrors.ToString + ",")
                    builder.Append("VB").AppendLine()
                    OutTextBox.Text += "Done" + vbCrLf
                    OutTextBox.Refresh()
                Next
            Next
        End If
        file.WriteLine(builder)
        errorsFile.WriteLine(errorBuilder)
        errorsFile.Close()
        file.Close()
        Dim myProcess As New Process
        If (epsiloNoCheck.Checked) Then
            OutTextBox.Clear()
            OutTextBox.Text += "Epsilon Benchmarking (No Cache) Started..." + vbCrLf
            For Each modelInList As Object In allModelsList
                For repeatExperiment As Integer = 1 To numberOfRepeats
                    Dim project = projects.Item("Reference", modelInList)
                    OutTextBox.Text += "Model: " + project.Property("Name").ToString + "... "
                    OutTextBox.Refresh()
                    id = project.Property("Id")
                    Dim numOfElements = project.Item("Dictionary", "Dictionary").ItemCount("")
                    Dim ProcessProperties As New ProcessStartInfo
                    ProcessProperties.FileName = "cmd.exe"
                    ProcessProperties.Arguments = "/c Java -jar -Djava.library.path=" + pathTxt.Text + " " + pathTxt.Text + "/epsilon.jar " + id + " """ + modelInList + """ " + outputFilePath + " " + errorsFilePath + " " + pathTxt.Text + " " + numOfElements.ToString + " " + repeatExperiment.ToString + " false false"
                    myProcess.Start(ProcessProperties).WaitForExit()
                    OutTextBox.Text += "Done" + vbCrLf
                    OutTextBox.Refresh()
                Next
            Next
        End If
        If (epsilonAttrCheck.Checked) Then
            OutTextBox.Clear()
            OutTextBox.Text += "Epsilon Benchmarking (Attributes Only Cache) Started..." + vbCrLf
            For Each modelInList As Object In allModelsList
                For repeatExperiment As Integer = 1 To numberOfRepeats
                    Dim project = projects.Item("Reference", modelInList)
                    OutTextBox.Text += "Model: " + project.Property("Name").ToString + "... "
                    OutTextBox.Refresh()
                    id = project.Property("Id")
                    Dim numOfElements = project.Item("Dictionary", "Dictionary").ItemCount("")
                    Dim ProcessProperties As New ProcessStartInfo
                    ProcessProperties.FileName = "cmd.exe"
                    ProcessProperties.Arguments = "/c Java -jar -Djava.library.path=" + pathTxt.Text + " " + pathTxt.Text + "/epsilon.jar " + id + " """ + modelInList + """ " + outputFilePath + " " + errorsFilePath + " " + pathTxt.Text + " " + numOfElements.ToString + " " + repeatExperiment.ToString + " true false"
                    myProcess.Start(ProcessProperties).WaitForExit()
                    OutTextBox.Text += "Done" + vbCrLf
                    OutTextBox.Refresh()
                Next
            Next
        End If
        If (epsilonValuesCheck.Checked) Then
            OutTextBox.Clear()
            OutTextBox.Text += "Epsilon Benchmarking (Values Only Cache) Started..." + vbCrLf
            For Each modelInList As Object In allModelsList
                For repeatExperiment As Integer = 1 To numberOfRepeats
                    Dim project = projects.Item("Reference", modelInList)
                    OutTextBox.Text += "Model: " + project.Property("Name").ToString + "... "
                    OutTextBox.Refresh()
                    id = project.Property("Id")
                    Dim numOfElements = project.Item("Dictionary", "Dictionary").ItemCount("")
                    Dim ProcessProperties As New ProcessStartInfo
                    ProcessProperties.FileName = "cmd.exe"
                    ProcessProperties.Arguments = "/c Java -jar -Djava.library.path=" + pathTxt.Text + " " + pathTxt.Text + "/epsilon.jar " + id + " """ + modelInList + """ " + outputFilePath + " " + errorsFilePath + " " + pathTxt.Text + " " + numOfElements.ToString + " " + repeatExperiment.ToString + " false true"
                    myProcess.Start(ProcessProperties).WaitForExit()
                    OutTextBox.Text += "Done" + vbCrLf
                    OutTextBox.Refresh()
                Next
            Next
        End If
        If (epsilonFullCheck.Checked) Then
            OutTextBox.Clear()
            OutTextBox.Text += "Epsilon Benchmarking (Both Caches) Started..." + vbCrLf
            For Each modelInList As Object In allModelsList
                For repeatExperiment As Integer = 1 To numberOfRepeats
                    Dim project = projects.Item("Reference", modelInList)
                    OutTextBox.Text += "Model: " + project.Property("Name").ToString + "... "
                    OutTextBox.Refresh()
                    id = project.Property("Id")
                    Dim numOfElements = project.Item("Dictionary", "Dictionary").ItemCount("")
                    Dim ProcessProperties As New ProcessStartInfo
                    ProcessProperties.FileName = "cmd.exe"
                    ProcessProperties.Arguments = "/c Java -jar -Djava.library.path=" + pathTxt.Text + " " + pathTxt.Text + "/epsilon.jar " + id + " """ + modelInList + """ " + outputFilePath + " " + errorsFilePath + " " + pathTxt.Text + " " + numOfElements.ToString + " " + repeatExperiment.ToString + " true true"
                    myProcess.Start(ProcessProperties).WaitForExit()
                    OutTextBox.Text += "Done" + vbCrLf
                    OutTextBox.Refresh()
                Next
            Next
        End If
    End Sub
    Private Function CheckConstraint1(dictionary As Object)
        Dim errorBuilder As New StringBuilder
        Dim c
        Dim Number As Integer
        Dim classes = dictionary.Items("Class")
        Do While classes.MoreItems
            c = classes.NextItem
            Dim cName = c.Property("Name")
            If ((Not Integer.TryParse(cName.Substring(0, 1), Number)) And (Not Char.IsUpper(cName, 0))) Then
                errorBuilder.AppendLine("[VB],Class " + cName + " (" + c.Property("Id") + ") does not start with uppercase.,[#1]")
                numberOfTotalErrors += 1
            End If
        Loop
        Return errorBuilder.ToString
    End Function
    Private Function CheckConstraint2(dictionary As Object)
        Dim errorBuilder As New StringBuilder
        Dim a
        Dim Number As Integer
        Dim attributes = dictionary.Items("Attribute")
        Do While attributes.MoreItems
            a = attributes.NextItem
            Dim aName = a.Property("Name")
            If ((Not Integer.TryParse(aName.Substring(0, 1), Number)) And (Char.IsUpper(aName, 0))) Then
                errorBuilder.AppendLine("[VB],Attribute " + aName + " (" + a.Property("Id") + ") should not start with uppercase.,[#2]")
                numberOfTotalErrors += 1
            End If
        Loop
        Return errorBuilder.ToString
    End Function
    Private Function CheckConstraint3(dictionary As Object)
        Dim errorBuilder As New StringBuilder
        Dim c
        Dim classes = dictionary.Items("Class")
        Do While classes.MoreItems
            c = classes.NextItem
            Dim cName = c.Property("Name")
            If (c.ItemCount("Operation") > 7) Then
                errorBuilder.AppendLine("[VB],Class " + cName + " (" + c.Property("Id") + ") has more than 7 operations.,[#3]")
                numberOfTotalErrors += 1
            End If
        Loop
        Return errorBuilder.ToString
    End Function
    Private Function CheckConstraint4(dictionary As Object)
        Dim errorBuilder As New StringBuilder
        Dim o
        Dim operations = dictionary.Items("Operation")
        Do While operations.MoreItems
            o = operations.NextItem
            Dim oName = o.Property("Name")
            If (o.ItemCount("Parameter") > 7) Then
                errorBuilder.AppendLine("[VB],Operation " + oName + " (" + o.Property("Id") + ") has more than 7 parameters.,[#4]")
                numberOfTotalErrors += 1
            End If
        Loop
        Return errorBuilder.ToString
    End Function
    Private Function CheckConstraint5(dictionary As Object)
        Dim errorBuilder As New StringBuilder
        Dim p
        Dim packages = dictionary.Items("Package")
        Do While packages.MoreItems
            p = packages.NextItem
            Dim pName = p.Property("Name")
            If (p.ItemCount("OwnedContents") = 0) Then
                errorBuilder.AppendLine("[VB],Package " + pName + " (" + p.Property("Id") + ") is empty.,[#5]")
                numberOfTotalErrors += 1
            End If
        Loop
        Return errorBuilder.ToString
    End Function
    Private Function CheckConstraint6(dictionary As Object)
        Dim errorBuilder As New StringBuilder
        Dim c
        Dim classes = dictionary.Items("Class")
        Do While classes.MoreItems
            c = classes.NextItem
            Dim cName = c.Property("Name")
            Dim superClasses = c.Items("SuperClass")
            'Dim numOfSuperClasses = c.ItemCount("SuperClass")
            Dim numOfNonInterfaces = 0
            Dim s
            Do While superClasses.MoreItems
                s = superClasses.NextItem
                If (s.Property("IsInterface") = "FALSE") Then
                    numOfNonInterfaces += 1
                End If
            Loop
            If (numOfNonInterfaces > 1) Then
                errorBuilder.AppendLine("[VB],Class " + cName + " (" + c.Property("Id") + ") has multiple inheritance.,[#6]")
                numberOfTotalErrors += 1
            End If
        Loop
            Return errorBuilder.ToString
    End Function
    Private Function CheckConstraint7(dictionary As Object)
        Dim errorBuilder As New StringBuilder
        Dim a
        Dim associations = dictionary.Items("Association")
        Do While associations.MoreItems
            a = associations.NextItem
            Dim aName = a.Property("Name")
            If (a.Property("Aggregate") = "Start") Then
                If (a.Property("EndMultiplicityUML") <> "1") Then
                    errorBuilder.AppendLine("[VB],Aggregation " + aName + " (" + a.Property("Id") + ") has multiplicity different than 1.,[#7]")
                    numberOfTotalErrors += 1
                End If
            End If
        Loop
        Return errorBuilder.ToString
    End Function
    Private Function CheckConstraint8a(dictionary As Object)
        Dim errorBuilder As New StringBuilder
        Dim a
        Dim associations = dictionary.Items("Association")
        Do While associations.MoreItems
            a = associations.NextItem
            Dim aName = a.Property("Name")
            Dim startMultiplicity = a.Property("StartMultiplicityUML")
            If (Regex.IsMatch(startMultiplicity, "(-)?[0-9]+\.{2}(-)?[0-9]+")) Then
                Dim splitMultiplicity = startMultiplicity.Split(New String() {".."}, StringSplitOptions.None)
                Dim lowerBound = splitMultiplicity(0)
                Dim upperBound = splitMultiplicity(1)
                If (lowerBound > upperBound) Then
                    errorBuilder.AppendLine("[VB],Lower bound is bigger than upper bound in the start of association " + aName + " (" + a.Property("Id") + ").,[#8a]")
                    numberOfTotalErrors += 1
                End If
            End If
        Loop
        Return errorBuilder.ToString
    End Function
    Private Function CheckConstraint8b(dictionary As Object)
        Dim errorBuilder As New StringBuilder
        Dim a
        Dim associations = dictionary.Items("Association")
        Do While associations.MoreItems
            a = associations.NextItem
            Dim aName = a.Property("Name")
            Dim endMultiplicity = a.Property("EndMultiplicityUML")
            If (Regex.IsMatch(endMultiplicity, "(-)?[0-9]+\.{2}(-)?[0-9]+")) Then
                Dim splitMultiplicity = endMultiplicity.Split(New String() {".."}, StringSplitOptions.None)
                Dim lowerBound = splitMultiplicity(0)
                Dim upperBound = splitMultiplicity(1)
                If (lowerBound > upperBound) Then
                    errorBuilder.AppendLine("[VB],Lower bound is bigger than upper bound in the end of association " + aName + " (" + a.Property("Id") + ").,[#8b]")
                    numberOfTotalErrors += 1
                End If
            End If
        Loop
        Return errorBuilder.ToString
    End Function
    Private Function CheckConstraint9a(dictionary As Object)
        Dim errorBuilder As New StringBuilder
        Dim a
        Dim associations = dictionary.Items("Association")
        Do While associations.MoreItems
            a = associations.NextItem
            Dim aName = a.Property("Name")
            Dim startMultiplicity = a.Property("StartMultiplicityUML")
            If (Regex.IsMatch(startMultiplicity, "(-)?[0-9]+\.{2}(-)?[0-9]+")) Then
                Dim splitMultiplicity = startMultiplicity.Split(New String() {".."}, StringSplitOptions.None)
                Dim upperBound = splitMultiplicity(1)
                If (upperBound <= 0) Then
                    errorBuilder.AppendLine("[VB],Upper bound in the start of association " + aName + " (" + a.Property("Id") + ") must be a positive integer.,[#9a]")
                    numberOfTotalErrors += 1
                End If
            End If
        Loop
        Return errorBuilder.ToString
    End Function
    Private Function CheckConstraint9b(dictionary As Object)
        Dim errorBuilder As New StringBuilder
        Dim a
        Dim associations = dictionary.Items("Association")
        Do While associations.MoreItems
            a = associations.NextItem
            Dim aName = a.Property("Name")
            Dim endMultiplicity = a.Property("EndMultiplicityUML")
            If (Regex.IsMatch(endMultiplicity, "(-)?[0-9]+\.{2}(-)?[0-9]+")) Then
                Dim splitMultiplicity = endMultiplicity.Split(New String() {".."}, StringSplitOptions.None)
                Dim upperBound = splitMultiplicity(1)
                If (upperBound <= 0) Then
                    errorBuilder.AppendLine("[VB],Upper bound in the end of association " + aName + " (" + a.Property("Id") + ") must be a positive integer.,[#9b]")
                    numberOfTotalErrors += 1
                End If
            End If
        Loop
        Return errorBuilder.ToString
    End Function
    Private Sub WarmUpDB(dictionary As Object)
        OutTextBox.Text += "Warming Up DB... "
        OutTextBox.Refresh()
        Dim consraints = dictionary.Items("Constraint")
        OutTextBox.Text += "Done" + vbCrLf
        OutTextBox.Refresh()
    End Sub
    Private Sub CheckContraints(constraints As Object)
        Dim c
        constraints.ResetQueryItems
        Dim pattern As String = "[0-9]+"

        Dim s1 As Stopwatch = Stopwatch.StartNew
        Do While constraints.MoreItems
            c = constraints.NextItem
            'Console.WriteLine(c.Property("Name"))
            ' Has Stereotype
            Dim hasS = HasStereotype(c, "Software Requirement")
            If Not hasS Then
                OutTextBox.Text += "Constraint " + c.Property("Name") + "Is Not a Software Requirement." + vbCrLf
                'Console.WriteLine("Constraint " + c.Property("Name") + "Is Not a Software Requirement.")
            Else
                If Not Regex.IsMatch(c.Property("Name"), pattern) Then
                    OutTextBox.Text += "Constraint's name (" + c.Property("Name") + ") does not follow the valid format." + vbCrLf
                End If
            End If
        Loop
        s1.Stop()
        OutTextBox.Text += "Time: " + s1.Elapsed.TotalMilliseconds.ToString + vbCrLf
    End Sub

    Private Sub CheckOperations(operations As Object)
        Console.ResetColor()

        Dim pattern As String = "[A-Z][a-z-A-Z0-9_]*"
        operations.ResetQueryItems
        Dim o
        Do While operations.MoreItems
            o = operations.NextItem
            OutTextBox.ForeColor = Color.Orange ' Warnings
            If Not Regex.IsMatch(o.Property("Name"), pattern) Then
                OutTextBox.Text += "Operation's name (" + o.Property("Name") + ") does not follow the valid format." + vbCrLf
            End If
            OutTextBox.ForeColor = Color.Red      ' Errors
            If o.Property("Description") = String.Empty Then
                OutTextBox.Text += "Operation " + o.Property("Name") + " does not have a description." + vbCrLf
            End If
            If o.Property("Return Type") = String.Empty Then
                OutTextBox.Text += "Operation " + o.Property("Name") + " has an invalid type assignment." + vbCrLf
            End If
            If HasStereotype(o, "Ada Declaration") Then
                If o.Property("Ada Declaration Text") = String.Empty Then
                    OutTextBox.Text += "Operation " + o.Property("Name") + " has an the <<Ada Declaration>> stereotype but its <’Ada Declaration Text’> is empty" + vbCrLf
                End If
            End If
        Loop
    End Sub

    Private Sub CheckAttribtues(attributes As Object)
        Console.ResetColor()
        Dim patternRO As String = "[A-Z][A-Z0-9_]*"
        Dim pattern As String = "[a-z][a-z-A-Z0-9_]*"
        Dim a
        attributes.ResetQueryItems
        Do While attributes.MoreItems
            a = attributes.NextItem
            Dim c = a.Item("Class")
            If Not c Is Nothing And HasStereotype(c, "SPARK Class") Then
                OutTextBox.ForeColor = Color.Orange ' Warnings
                If a.Property("Read Only") = "TRUE" Then
                    If Not Regex.IsMatch(a.Property("Name"), patternRO) Then
                        OutTextBox.Text += "Attribute " + a.Property("Name") + " is Read Only and it's name does not follow the valid format." + vbCrLf
                    End If
                Else
                    If Not Regex.IsMatch(a.Property("Name"), pattern) Then
                        OutTextBox.Text += "Attribute " + a.Property("Name") + " is NOT Read Only and it's name does not match the valid format." + vbCrLf
                    End If
                End If
                If a.Property("Description") = String.Empty Then
                    OutTextBox.Text += "Attribute " + a.Property("Name") + " does not have a description." + vbCrLf
                End If
                OutTextBox.ForeColor = Color.Red     ' Errors
                If a.Property("Data Type") = String.Empty Then
                    OutTextBox.Text += "Attribute " + a.Property("Name") + " does not have a type." + vbCrLf
                End If
                If a.Property("Visibility") = "Public" Then
                    OutTextBox.Text += "Attribute " + a.Property("Name") + " has <Public> visibility." + vbCrLf
                End If
                If Not a.Property("Storage") = "On Class" Then
                    OutTextBox.Text += "Attribute " + a.Property("Name") + " has no <On Class> storage." + vbCrLf
                End If
            End If
        Loop
    End Sub

    Private Function HasStereotype(item As Object, stereotype As String)
        If item Is Nothing Then
            Return False
        End If
        If Not item.Item("Stereotype", stereotype) Is Nothing Then
            Return True
        End If
        Return False
    End Function

    Private Sub browseBtn_Click(sender As Object, e As EventArgs) Handles browseBtn.Click
        Dim dialog = New FolderBrowserDialog()
        dialog.SelectedPath = Application.StartupPath
        If DialogResult.OK = dialog.ShowDialog() Then
            pathTxt.Text = dialog.SelectedPath
        End If
    End Sub

    Private Function GetIdFromNameAndId(nameAndId As String)
        Dim splitString = nameAndId.Split(New String() {"(id:"}, StringSplitOptions.None)
        Return splitString(1)
    End Function
End Class
