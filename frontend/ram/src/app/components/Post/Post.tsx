"use client";
import {
  Card,
  createTheme,
  Group,
  MantineProvider,
  Text,
  Title,
  Typography,
} from "@mantine/core";
import { useMemo } from "react";
import { PostData } from "../../lib/definitions";
import { sanitizePostContent } from "../../lib/sanitizePostContent";
import "./styles.scss";

type PostProps = {
  post: PostData;
  allowHtmlMarkup: boolean;
};

const theme = createTheme({
  headings: {
    textWrap: "wrap",
  },
  components: {
    Title: {
      styles: () => ({
        root: {
          textTransform: "capitalize",
        },
      }),
    },
  },
});

export default function Post({ post }: PostProps) {
  const content = useMemo(
    () => sanitizePostContent(post.content),
    [post.content],
  );

  return (
    <MantineProvider theme={theme}>
      <Card padding="lg" styles={{ root: { marginBottom: 10 } }} id={post.id}>
        <Group justify="center" mb="md" align="right">
          <Title order={1}>{post.title}</Title>
        </Group>

        <Typography className="post-content">
          <div dangerouslySetInnerHTML={{ __html: content }} />
        </Typography>
        <br />
        <Text size="xs" ta="right">
          {post.author}
        </Text>
        <Text size="xs" ta="right">
          {post.createdOn}
        </Text>
      </Card>
    </MantineProvider>
  );
}
