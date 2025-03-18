import { Card, Image, Box, Text, Group } from "@mantine/core";

export default function Post({
  post,
}: {
  post: { title: string; content: string; author: string };
}) {
  return (
    <>
      <Card shadow="sm" padding="lg" radius="md" withBorder>
        <Image
          src="https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/images/bg-8.png"
          height={10}
          w="auto"
          radius="md"
          fit="contain"
        />

        <Group justify="space-between" mt="md" mb="xs">
          <Text fw={500}>{post.author}</Text>
        </Group>

        <Text size="sm" c="dimmed">
          {post.content}
        </Text>
      </Card>
    </>
  );
}
