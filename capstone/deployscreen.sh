#!/bin/bash
set -e

echo "=============================="
echo " Starting MAC Deployment Flow "
echo "=============================="

# -----------------------------
# Paths
# -----------------------------
POM_PATH="/home/mohansaikumar/Desktop/Capstone/capstone/pom.xml"
JAR_PATH="/home/mohansaikumar/Desktop/Capstone/capstone/target/onbCBean.jar"

JBOSS_HOME="/home/mohansaikumar/Documents/MACK-SHORE 13/MACK-SHORE"
DEPLOYMENTS_DIR="$JBOSS_HOME/standalone/deployments"
WAR_DIR="$DEPLOYMENTS_DIR/MAC.war"
LIB_DIR="$WAR_DIR/WEB-INF/lib"

DEPLOYED_MARKER="$DEPLOYMENTS_DIR/MAC.war.deployed"
FAILED_MARKER="$DEPLOYMENTS_DIR/MAC.war.failed"
ISDEPLOYING_MARKER="$DEPLOYMENTS_DIR/MAC.war.isdeploying"

SOURCE_DODEPLOY="$JBOSS_HOME/standalone/MAC.war.dodeploy"
VDS_DODEPLOY="$JBOSS_HOME/standalone/vds.war.dodeploy"

TARGET_DODEPLOY="$DEPLOYMENTS_DIR/MAC.war.dodeploy"

# -----------------------------
# Step 1: Maven clean
# -----------------------------
echo "[1/6] Running mvn clean..."
mvn clean -f "$POM_PATH"

# -----------------------------
# Step 2: Maven package
# -----------------------------
echo "[2/6] Running mvn package..."
mvn package -f "$POM_PATH"

# -----------------------------
# Step 3: Copy JAR
# -----------------------------
echo "[3/6] Copying onbCBean.jar..."
cp -f "$JAR_PATH" "$LIB_DIR/"

# -----------------------------
# Step 4: Cleanup markers
# -----------------------------
echo "[4/6] Cleaning old deployment markers..."
rm -f "$DEPLOYED_MARKER" "$FAILED_MARKER" "$ISDEPLOYING_MARKER"
sleep 2

# -----------------------------
# Step 5: Trigger redeploy
# -----------------------------
echo "[5/6] Triggering redeploy..."

if [ ! -f "$SOURCE_DODEPLOY" ]; then
    echo "❌ Source dodeploy not found: $SOURCE_DODEPLOY"
    exit 1
fi

cp -f "$SOURCE_DODEPLOY" "$TARGET_DODEPLOY"
echo "✅ Copied AND PASTED dodeploy from source to target."
# cp -f "$VDS_DODEPLOY" "$TARGET_DODEPLOY"
sleep 2
# -----------------------------
# Step 6: Done
# -----------------------------
echo "[6/6] Triggering Redeploy script completed."

echo "Exact time: $(date '+%d-%Y-%m %H:%M:%S')"
echo "=============================="

