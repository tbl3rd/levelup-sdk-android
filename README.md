# LevelUp SDK Android

The LevelUp SDK for Android provides client-server communication with the LevelUp web service. 

## See It in Action

The easiest way to get started using the SDK is to look at our
[sample app](https://github.com/TheLevelUp/levelup-sdk-sample-android). It's easy to check out the
code:

```
git clone git@github.com:TheLevelUp/levelup-sdk-sample-android.git
```

## Using the SDK

For documentation about the LevelUp platform and SDKs, see the
[LevelUp developer site](http://developer.thelevelup.com). For Android library-specific code-level
documentation, see the documentation for the [Deeplink Auth Library](levelUpDeeplinkAuthLib) and 
[Core Library](levelUpCoreLib).

## Including the SDK

Make sure `mavenCentral()` or `jcenter()` are in your top-level `build.gradle`:

```
allprojects {
    repositories {
        mavenCentral()
    }
}
```

Then add the SDK components to your app project's `build.gradle`:

```
dependencies {
    compile('com.scvngr:levelup-sdk-android-core:2.1.2@aar') { transitive = true }
    compile('com.scvngr:levelup-sdk-android-deeplink-auth:2.1.2@aar') { transitive = true }
}
```

You can omit the `@aar` and `{ transitive = true }` if you're not using Gradle 1.12; these are only
needed to work around a [bug in that version](http://issues.gradle.org/browse/GRADLE-3081).

## SDK development

If you're developing the SDK itself, you can publish to your local Maven repository by adding
`mavenLocal()` to the `repositories` block. Run `levelUpCoreLib:installArchives` and
`levelUpDeeplinkAuthLib:installArchives` to publish the necessary build artifacts locally.

You can also include the SDK as a git submodule and use its projects directly; see
[settings-example-submodule-usage.gradle](settings-example-submodule-usage.gradle).

To use Sonatype -SNAPSHOT builds, use the following in the `repositories {` section:
```
maven { url 'https://oss.sonatype.org/content/groups/public' }
```

To use Sonatype staged builds not published to Maven Central:
```
maven { url 'https://oss.sonatype.org/content/groups/staging' }
```

# Developer Terms

By enabling LevelUp integrations, including through this SDK, you agree to LevelUp's
[developer terms](https://www.thelevelup.com/developer-terms).

# License
 
Copyright (C) 2014 SCVNGR, Inc. d/b/a LevelUp

Except as otherwise noted, the LevelUp SDK for Android is licensed under the Apache License, Version
2.0 (http://www.apache.org/licenses/LICENSE-2.0.html).

Unless required by applicable law or agreed to in writing, software distributed under the License is
distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
implied. See the License for the specific language governing permissions and limitations under the
License.
