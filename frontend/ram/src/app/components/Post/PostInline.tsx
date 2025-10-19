import { Card, CardSection, Group, Text, Title } from "@mantine/core";
import Link from "next/link";
import sanitizeHtml from "sanitize-html";
import { PostData } from "../../lib/definitions";

export default function PostInline(post: PostData) {
  const sanitizedContent = sanitizeHtml(post.content, {
    allowedTags: [],
    allowedAttributes: {},
  });

  return (
    <Card
      w="100%"
      shadow="sm"
      padding="lg"
      radius="md"
      withBorder
      component={Link}
      href={`/posts/${post.titleUrl}`}
      styles={{ root: { marginBottom: 10 } }}
      id={post.id}
    >
      <CardSection></CardSection>
      <Group justify="space-between" mt="md" mb="xs">
        <Title order={5}>{post.title}</Title>
      </Group>
      <Text bottom={10}>{sanitizedContent}</Text>

      <Text size="xs" ta="right" mt={10}>
        {post.author}, {post.createdOn}
      </Text>
    </Card>
  );
}
