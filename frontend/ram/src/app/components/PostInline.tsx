import {
  Card,
  Image,
  Box,
  Text,
  Group,
  Title,
  CardSection,
  Divider,
  NavLink,
} from "@mantine/core";
import { PostData } from "../lib/definitions";
import sanitizeHtml from "sanitize-html";
import Link from "next/link";

export default function PostInline(post: PostData) {
  let displayContent;

  const sanitizedContent = sanitizeHtml(post.content, {
    allowedTags: [],
    allowedAttributes: {},
  });

  return (
    <>
      <Card
        shadow="sm"
        padding="lg"
        radius="md"
        withBorder
        component={Link}
        href={`/posts/${post.title}`}
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
    </>
  );
}
