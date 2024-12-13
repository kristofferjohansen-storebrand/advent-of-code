package no.krazyglitch.aoc2024.day9;

import no.krazyglitch.util.FileUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static no.krazyglitch.util.DateUtils.getMillisSince;

public class DiskFragmenter {

    public DiskFragmenter() {
        try {
            final String data = FileUtil.readInputFile(this.getClass()).getFirst();
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("The checksum of the defragmented disk is %d\n", getDefragmentedChecksum(data));
            System.out.printf("Part one took %d ms\n\n", getMillisSince(start));

            start = LocalDateTime.now();
            System.out.printf("The checksum of the defragmented disk when moving entire files is %d\n", getDefragmentedChecksumRetainingFiles(data));
            System.out.printf("Part two took %d ms", getMillisSince(start));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static long getDefragmentedChecksum(final String data) {
        final DiskData diskData = parseData(data);
        final List<Integer> defraggedList = defragmentDisk(diskData);
        final int lastBlock = defraggedList.indexOf(-1);

        return IntStream.range(0, lastBlock)
                .mapToLong(index -> defraggedList.get(index) * index)
                .sum();
    }

    public static long getDefragmentedChecksumRetainingFiles(final String data) {
        final List<File> files = parseFiles(data);
        final List<File> degfragmentedFiles = getDefragmentedDisk(files);

        long sum = 0;
        int blockIndex = 0;
        for (int i = 0; i < degfragmentedFiles.size(); i++) {
            final File file = degfragmentedFiles.get(i);
            if (file.id() != -1) {
                for (int j = blockIndex; j < blockIndex + file.size(); j++) {
                    sum += (long) file.id() * j;
                }
            }

            blockIndex += file.size();
        }

        return sum;
    }

    private static List<File> getDefragmentedDisk(final List<File> files) {
        final List<File> defragmentedFiles = new ArrayList<>(files);

        for (int i = 0; i < defragmentedFiles.size(); i++) {
            final File emptyFile = defragmentedFiles.get(i);
            if (emptyFile.id() == -1) {
                final FileData replacementFile = getSuitableFile(defragmentedFiles, emptyFile.size(), i);

                if (replacementFile != null) {
                    final int remainingSpace = emptyFile.size() - replacementFile.file().size();
                    defragmentedFiles.set(i, replacementFile.file());
                    defragmentedFiles.set(replacementFile.position(), new File(-1, replacementFile.file.size()));

                    if (remainingSpace > 0) {
                        defragmentedFiles.add(i + 1, new File(-1, remainingSpace));
                    }
                }
            }
        }

        return defragmentedFiles;
    }

    private static FileData getSuitableFile(final List<File> files, final int maximumSize, final int head) {
        for (int i = files.size() - 1; i > head && i > 0; i--) {
            final File file = files.get(i);
            if (file.id() == -1) {
                continue;
            }

            if (file.size() <= maximumSize) {
                return new FileData(file, i);
            }
        }

        return null;
    }

    private static List<Integer> defragmentDisk(final DiskData diskData) {
        final int[] blocks = diskData.blocks().stream().mapToInt(i -> i).toArray();
        int head = 0;
        boolean gapsFilled = false;

        while (!gapsFilled) {
            final int block = blocks[head];
            if (block == -1) {
                for (int searchIndex = blocks.length - 1; searchIndex > 0; searchIndex--) {
                    if (searchIndex <= head) {
                        gapsFilled = true;
                        break;
                    }

                    final int searchBlock = blocks[searchIndex];
                    if (searchBlock != -1) {
                        blocks[head] = searchBlock;
                        blocks[searchIndex] = -1;
                        break;
                    }
                }
            }

            head++;
        }

        return Arrays.stream(blocks)
                .boxed()
                .toList();
    }

    private static DiskData parseData(final String data) {
        final int diskSize = data.chars().map(Character::getNumericValue).sum();
        final List<Integer> blocks = new ArrayList<>(diskSize);

        for (int i = 0; i < data.length(); i++) {
            final int block = Character.getNumericValue(data.charAt(i));

            for (int blockIndex = 0; blockIndex < block; blockIndex++) {
                blocks.add(i % 2 == 0 ? i / 2 : -1);
            }
        }

        return new DiskData(blocks);
    }

    private static List<File> parseFiles(final String data) {
        final List<File> files = new ArrayList<>(data.length());

        IntStream.range(0, data.length()).forEach(index -> {
            final int block = Character.getNumericValue(data.charAt(index));
            files.add(new File(index % 2 == 0 ? index / 2 : -1, block));
        });

        return files;
    }

    private record FileData(File file, int position) {
    }

    private record File(int id, int size) {
    }

    private record DiskData(List<Integer> blocks) {
    }

    public static void main(final String[] args) {
        new DiskFragmenter();
    }
}
