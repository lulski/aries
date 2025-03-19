import {
  Card,
  Image,
  Box,
  Text,
  Group,
  Title,
  CardSection,
  Divider,
} from "@mantine/core";

export default function Post({
  post,
}: {
  post: { title: string; content: string; author: string };
}) {
  return (
    <>
      <Card w={400} shadow="sm" padding="lg" radius="md" withBorder>
        <CardSection>
          <Image src="https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/images/bg-10.png" />
        </CardSection>

        <Group justify="space-between" mt="md" mb="xs">
          <Title order={5}>{post.title}</Title>
        </Group>

        <Text size="sm" c="dimmed">
          {post.content}
        </Text>
        <br />
        <Text size="xs" c="dark" ta="right">
          {post.author}
        </Text>
      </Card>
      <Divider my="md" />
    </>
  );
}
