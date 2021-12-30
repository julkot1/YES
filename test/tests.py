import glob
import subprocess
import os


class Colors:
    HEADER = '\033[95m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    END = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'


def foundPrint(val: int):
    if val == 1:
        print("Found "+str(val)+" file to test.")
    else:
        print("Found "+str(val)+" files to test.")


def printErr(text: str):
    print(Colors.FAIL+text+Colors.END)
    quit()


def prFile(file: str):
    return Colors.UNDERLINE + file + Colors.END


def getInFiles():
    a = glob.glob("./in/*.yes")
    if len(a) == 0:
        printErr("No input files!")
    return a


def getOutFiles():
    a = glob.glob("./out/*")
    if len(a) == 0:
        printErr("No output files!")
    return a


def pairFiles(inFiles: list, outFiles: list):
    pairs = []
    for inFile in inFiles:
        for outFile in outFiles:
            if inFile[5:len(inFile)-4:] == outFile[6::]:
                pairs.append((inFile, outFile))
                outFiles.remove(outFile)
                break
        if len(pairs) == 0:
            printErr("Missing out file for "+prFile(inFile)+"!")
        if pairs[len(pairs)-1][0] != inFile:
            printErr("Missing out file for "+prFile(inFile)+"!")
    return pairs


def compileYES(path: str):
    print("Running "+prFile(path)+" test!")
    subprocess.call(['python3', '../yes.py', path],
                    stdout=subprocess.DEVNULL, stderr=subprocess.STDOUT)
    subprocess.run(['gcc', 'out.c', "-o", "test"])
    return subprocess.run(["./test"], capture_output=True).stdout.decode("utf-8")


def readExpectedOut(path: str):
    f = open(path)
    out = f.read()
    f.close()
    return out


def clear():
    subprocess.run(['rm', 'out.c'])
    subprocess.run(['rm', 'test'])


def passTest():
    print(Colors.OKGREEN+"PASS"+Colors.END)


def failTest():
    print(Colors.WARNING+"OUTPUT IS NOT CORRECT"+Colors.END)


def testOut(YESout, expectedOut):
    if YESout == expectedOut:
        passTest()
        return 1
    failTest()
    return 0


def test(pair: tuple):
    out = compileYES(pair[0])
    expectedOut = readExpectedOut(pair[1])
    clear()
    return testOut(out, expectedOut)


def main():
    print(Colors.HEADER+"Testing YES"+Colors.END)
    inFiles = getInFiles()
    outFiles = getOutFiles()
    pairs = pairFiles(inFiles, outFiles)
    foundPrint(len(pairs))
    count = 0
    for pair in pairs:
        count += test(pair)
    print(str(count)+"/"+str(len(pairs)))


if __name__ == "__main__":
    main()
