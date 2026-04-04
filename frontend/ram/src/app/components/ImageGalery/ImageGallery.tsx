import { Box, Group, Image, SimpleGrid, Stack, Text } from "@mantine/core";
import { Dropzone, DropzoneProps, IMAGE_MIME_TYPE } from "@mantine/dropzone";
import { IconPhoto, IconUpload, IconX } from "@tabler/icons-react";

type ImageGalleryProps = Partial<DropzoneProps> & {
  numImages?: number | 0;
};

export default function ImageGallery(props: ImageGalleryProps) {
  return (
    <>
      <Stack>
        <div style={{ resize: "horizontal", maxWidth: "100%" }}>
          <SimpleGrid
            type="container"
            cols={{ base: 1, "300px": 2, "500px": 5 }}
            spacing={{ base: 10, "300px": "xl" }}
          >
            {props.numImages &&
              props.numImages > 0 &&
              [...Array(props.numImages)].map((_, index) => (
                <Box
                  key={index}
                  p="sm"
                  bd="1px solid gray"
                  style={{
                    minHeight: "100px",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                  }}
                >
                  <Image
                    key={`img-${index}`}
                    radius="xs"
                    h={50}
                    w="auto"
                    fit="fill"
                    src="https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/images/bg-9.png"
                  />
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
