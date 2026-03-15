import { Box, Group, SimpleGrid, Stack, Text } from "@mantine/core";
import { Dropzone, DropzoneProps, IMAGE_MIME_TYPE } from "@mantine/dropzone";
import { IconPhoto, IconUpload, IconX } from "@tabler/icons-react";

export default function ImageGalery(props: Partial<DropzoneProps>) {
  return (
    <>
      <Stack>
        <div style={{ resize: "horizontal", maxWidth: "100%" }}>
          <SimpleGrid
            type="container"
            cols={{ base: 1, "300px": 2, "500px": 5 }}
            spacing={{ base: 10, "300px": "xl" }}
          >
            {[1, 2, 3, 4, 5].map((num) => (
              <Box
                key={num}
                p="md"
                bd="1px solid gray"
                style={{
                  minHeight: "100px",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                }}
              >
                {num}
              </Box>
            ))}
          </SimpleGrid>
        </div>

        <Box my="xl" bd={"1px solid gray"} p="xl">
          <Dropzone
            onDrop={(files) => console.log("accepted files", files)}
            onReject={(files) => console.log("rejected files", files)}
            maxSize={5 * 1024 ** 2}
            accept={IMAGE_MIME_TYPE}
            {...props}
          >
            <Group
              justify="center"
              gap="xl"
              mih={220}
              style={{ pointerEvents: "none" }}
            >
              <Dropzone.Accept>
                <IconUpload
                  size={52}
                  color="var(--mantine-color-blue-6)"
                  stroke={1.5}
                />
              </Dropzone.Accept>
              <Dropzone.Reject>
                <IconX
                  size={52}
                  color="var(--mantine-color-red-6)"
                  stroke={1.5}
                />
              </Dropzone.Reject>
              <Dropzone.Idle>
                <IconPhoto
                  size={52}
                  color="var(--mantine-color-dimmed)"
                  stroke={1.5}
                />
              </Dropzone.Idle>

              <div>
                <Text size="xl" inline>
                  Drag images here or click to select files
                </Text>
                <Text size="sm" c="dimmed" inline mt={7}>
                  Attach as many files as you like, each file should not exceed
                  5mb
                </Text>
              </div>
            </Group>
          </Dropzone>
        </Box>
      </Stack>
    </>
  );
}
